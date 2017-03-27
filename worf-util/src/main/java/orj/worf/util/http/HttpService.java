package orj.worf.util.http;

import java.util.Map;

import orj.worf.util.CharEncoding;

public final class HttpService {

    private HttpService() {
    }

    public static String sendRequest(final HttpRequest httpRequest) throws HttpClientException {
        try {
            return HttpClient.get().requestWithStringResult(httpRequest);
        } catch (Throwable e) {
            if (e instanceof HttpClientException) {
                throw (HttpClientException) e;
            } else {
                throw new HttpClientException(e);
            }
        }
    }

    public static String sendPostRequest(final String url, final Map<String, String> parameters)
            throws HttpClientException {
        return sendPostRequest(url, parameters, CharEncoding.UTF_8);
    }

    private static String sendPostRequest(final String url, final Map<String, String> parameters, final String charset)
            throws HttpClientException {
        HttpRequest req = new HttpRequest(url, charset);
        req.setMethod(HttpRequest.METHOD.POST);
        req.setParameters(parameters);
        try {
            return HttpClient.get().requestWithStringResult(req);
        } catch (Throwable e) {
            if (e instanceof HttpClientException) {
                throw (HttpClientException) e;
            } else {
                throw new HttpClientException(e);
            }
        }
    }

    private static String sendGetRequest(final String url, final Map<String, String> parameters, final String charset)
            throws HttpClientException {
        HttpRequest req = new HttpRequest(url, charset);
        req.setParameters(parameters);
        req.setMethod(HttpRequest.METHOD.GET);
        try {
            return HttpClient.get().requestWithStringResult(req);
        } catch (Throwable e) {
            if (e instanceof HttpClientException) {
                throw (HttpClientException) e;
            } else {
                throw new HttpClientException(e);
            }
        }
    }

    public static String sendGetRequest(final String url, final Map<String, String> parameters)
            throws HttpClientException {
        return sendGetRequest(url, parameters, CharEncoding.UTF_8);
    }

    public static String sendGetRequest(final String url) throws HttpClientException {
        return sendGetRequest(url, null, CharEncoding.UTF_8);
    }
}
