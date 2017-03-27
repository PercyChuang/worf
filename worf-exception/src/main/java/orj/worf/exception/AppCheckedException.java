package orj.worf.exception;

public class AppCheckedException extends BaseCheckedException {

    private static final long serialVersionUID = -5327469034079626074L;

    public AppCheckedException(final String code, final String defaultMessage, final Throwable cause,
            final Object... args) {
        super(code, defaultMessage, cause, args);
    }

    public AppCheckedException(final String code, final String defaultMessage, final Object... args) {
        super(code, defaultMessage, args);
    }

    public AppCheckedException(final String defaultMessage, final Throwable cause) {
        super(defaultMessage, cause);
    }

    public AppCheckedException(final String defaultMessage) {
        super(defaultMessage);
    }

}
