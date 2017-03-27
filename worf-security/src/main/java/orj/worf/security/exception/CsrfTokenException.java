package orj.worf.security.exception;

public class CsrfTokenException extends Exception {
    private static final long serialVersionUID = -2944250694601510416L;

    public CsrfTokenException(String defaultMessage, Throwable cause) {
        super(defaultMessage, cause);
    }

    public CsrfTokenException(String defaultMessage) {
        super(defaultMessage);
    }
}
