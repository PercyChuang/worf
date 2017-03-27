package orj.worf.util.http;

public class HttpClientException extends Exception {
    private static final long serialVersionUID = 3105630126342123965L;

    public HttpClientException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HttpClientException(final String message) {
        super(message);
    }

    public HttpClientException(final Throwable ex) {
    }

}
