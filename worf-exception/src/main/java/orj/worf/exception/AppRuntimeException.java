package orj.worf.exception;

public class AppRuntimeException extends BaseRuntimeException {

    private static final long serialVersionUID = -3577333812804703994L;

    public AppRuntimeException(final String code, final String defaultMessage, final Throwable cause,
            final Object... args) {
        super(code, defaultMessage, cause, args);
    }

    public AppRuntimeException(final String code, final String defaultMessage, final Object... args) {
        super(code, defaultMessage, args);
    }

    public AppRuntimeException(final String defaultMessage, final Throwable cause) {
        super(defaultMessage, cause);
    }

    public AppRuntimeException(final String defaultMessage) {
        super(defaultMessage);
    }

}
