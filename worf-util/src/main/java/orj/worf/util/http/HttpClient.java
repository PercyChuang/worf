package orj.worf.util.http;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * 适用数据量不大,有限长度的响应数据.以及服务器是已知可信任的.
 */
class HttpClient {
    private final XLogger logger = XLoggerFactory.getXLogger(HttpClient.class);

    /**
     * 连接闲置最大时间
     */
    private final int defaultIdleConnTimeout = 1800;

    /**
     * 每个route允许的最大连接数
     */
    private final int defaultMaxPerRoute = 30;

    /**
     * 客户端可以同时并行的最大连接数
     */
    private final int defaultMaxTotalConn = 15;

    /**
     * HTTP连接管理器
     */
    private final PoolingHttpClientConnectionManager connectionManager;
    /**
     * 用户不断轮询,处理超过闲置最大时间的连接
     */
    private final IdleConnectionMonitorThread ict;

    private static HttpClient httpClient;

    /**
     * 确保单例的工厂方法
     *
     * @throws HttpClientException
     */
    public synchronized static HttpClient get() throws HttpClientException {
        if (httpClient == null) {
            httpClient = new HttpClient();
        }
        return httpClient;
    }

    private HttpClient() throws HttpClientException {
        try {
            connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
            connectionManager.setMaxTotal(defaultMaxTotalConn);
            connectionManager.setDefaultConnectionConfig(ConnectionConfig.custom().setCharset(Consts.UTF_8).build());
            ict = new IdleConnectionMonitorThread(connectionManager, defaultIdleConnTimeout);
            ict.start();
        } catch (Throwable ex) {
            throw new HttpClientException(ex);
        }
        try {
            Runtime.getRuntime().addShutdownHook(new HttpClientShutdownHook(httpClient));
        } catch (Throwable t) {
        }
    }

    /**
     * 发送HTTP GET或POST请求,响应String数据,根据服务器端指定的字符集自动对byte[]进行编码识别
     * 注意:不适合响应数据量很大的情况,尤其是下载大文件 当使用GET方法时,必须且只能使用UTF-8字符集对URI进行编码
     *
     * @param request 包含请求信息的对象
     * @return 所有响应数据作为一个字符串返回
     * @throws HttpClientException
     *
     */
    public String requestWithStringResult(final HttpRequest request) throws HttpClientException {
        return requestWithCustomObjectResult(request, new StringResponseHandler());
    }

    /**
     * 发送HTTP GET或POST请求
     *
     * @param request 包含请求信息的对象
     * @return 响应数据
     * @throws HttpClientException
     *
     */
    public byte[] requestWithBytesResult(final HttpRequest request) throws HttpClientException {
        return requestWithCustomObjectResult(request, new BytesResponseHandler());
    }

    public <T> T requestWithCustomObjectResult(final HttpRequest request, final ResponseHandler<T> responseHandler)
            throws HttpClientException {
        HttpRequestBase method = null;
        CloseableHttpClient httpclient = initHttpClient(request);
        try {
            method = initHttpMethod(request);
            if (logger.isDebugEnabled()) {
                logger.debug("开始发送http请求:targetUrl[{}],parameters[{}]", request.getTargetUrl(), request.getParameters());
            }
            return httpclient.execute(method, responseHandler);
        } catch (Throwable ex) {
            throw new HttpClientException(ex);
        } finally {
            if (method != null) {
                try {
                    method.releaseConnection();
                } catch (Throwable ex) {
                }
            }
        }
    }

    /**
     * 初始化HttpClient
     */
    private CloseableHttpClient initHttpClient(final HttpRequest request) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.disableAuthCaching().disableRedirectHandling();
        return builder.setConnectionManager(connectionManager).build();
    }

    private HttpRequestBase initHttpMethod(final HttpRequest request) throws URISyntaxException,
            UnsupportedEncodingException {
        HttpRequestBase method;
        if (HttpRequest.METHOD.GET.equals(request.getMethod())) {
            method = initGet(request);
        } else {
            method = initPost(request);
        }
        RequestConfig reqConfig = RequestConfig.custom().setSocketTimeout(request.getTimeout())
                .setConnectTimeout(request.getConnectionTimeout())
                .setConnectionRequestTimeout(request.getConnectionTimeout()).build();
        method.setConfig(reqConfig);
        method.addHeader("User-Agent", "Mozilla/5.0 Chrome/26.0.1410.43 Safari/535.12");
        method.setHeader("Connection", "close");
        method.removeHeaders("Expect");
        return method;
    }

    /**
     * 初始化HTTP GET方法,必须且只能使用UTF-8字符集
     */
    private HttpRequestBase initGet(final HttpRequest request) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(request.getTargetUrl());
        Map<String, String> parameters = request.getParameters();
        for (String key : parameters.keySet()) {
            builder.addParameter(key, parameters.get(key));
        }
        return new HttpGet(builder.build());
    }

    /**
     * 初始化post请求
     */
    private HttpRequestBase initPost(final HttpRequest request) throws UnsupportedEncodingException {
        HttpPost method = new HttpPost(request.getTargetUrl());
        Map<String, String> parameters = request.getParameters();
        List<NameValuePair> nvps = new ArrayList<NameValuePair>(parameters.size());
        for (String key : parameters.keySet()) {
            nvps.add(new BasicNameValuePair(key, parameters.get(key)));
        }
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps, request.getCharset());
        method.setEntity(urlEncodedFormEntity);
        method.addHeader("Content-Type",
                "application/x-www-form-urlencoded; text/html; charset=" + request.getCharset());
        return method;
    }

    /**
     * 将NameValuePairs数组转变为字符串
     */
    protected String toString(final NameValuePair[] nameValues) {
        if (nameValues == null || nameValues.length == 0) {
            return "null";
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < nameValues.length; i++) {
            NameValuePair nameValue = nameValues[i];
            if (i == 0) {
                buffer.append(nameValue.getName() + "=" + nameValue.getValue());
            } else {
                buffer.append("&" + nameValue.getName() + "=" + nameValue.getValue());
            }
        }
        return buffer.toString();
    }

    /**
     * 释放客户端占用的资源,需要在整个系统关闭时调用
     */
    public void close() {
        ict.shutdown();
        connectionManager.shutdown();
    }
}
