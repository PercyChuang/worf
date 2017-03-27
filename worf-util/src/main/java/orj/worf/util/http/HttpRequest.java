package orj.worf.util.http;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

import orj.worf.util.CharEncoding;

/**
 * HTTP GET或POST请求对象 注意:建议使用POST请求
 *
 *
 */
public class HttpRequest {

    public static enum METHOD {
        POST, GET;
    }

    /**
     * 连接超时时间
     */
    private final int DEFAULT_CONNECTION_TIMEOUT = 10 * 1000;
    /**
     * 响应超时时间
     */
    private final int DEFAULT_SO_TIMEOUT = 10 * 1000;

    public static String DEFAULT_CHARSET = CharEncoding.UTF_8;
    /**
     * 目标URL,不包括queryString
     */
    private final String targetUrl;
    /**
     * 默认的请求方式
     */
    private METHOD method = METHOD.POST;
    /**
     * 等待响应的超时时间, 单位:毫秒.0表示不超时,一直等待
     */
    private int timeout = DEFAULT_SO_TIMEOUT;
    /**
     * 等待连接成功的最大时间,单位:毫秒.0表示不超时,一直等待
     */
    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    /**
     * 请求的参数名和参数值
     */
    private Map<String, String> parameters;
    /**
     * 默认的请求编码方式
     */
    private String charset = DEFAULT_CHARSET;

    /**
     * @param targetUrl 目标URL,不包括queryString
     */
    public HttpRequest(final String targetUrl) {
        this(targetUrl, DEFAULT_CHARSET);
    }

    public HttpRequest(final String targetUrl, String charset) {
        this.targetUrl = targetUrl;
        charset = charset == null ? DEFAULT_CHARSET : charset;
        Charset.forName(charset);
        this.charset = charset;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getParameters() {
        return parameters == null ? Collections.EMPTY_MAP : parameters;
    }

    public void setParameters(final Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(final int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(final String charset) {
        this.charset = charset;
    }

    public METHOD getMethod() {
        return method;
    }

    public void setMethod(final METHOD method) {
        this.method = method;
    }

}
