package orj.worf.exception;

public class SecurityCheckedException extends BaseCheckedException {

    private static final long serialVersionUID = 7589423398813215488L;

    public SecurityCheckedException(final String code, final String defaultMessage, final Throwable cause,
            final Object... args) {
        super(code, defaultMessage, cause, args);
    }

    public SecurityCheckedException(final String code, final String defaultMessage, final Object... args) {
        super(code, defaultMessage, args);
    }

    public SecurityCheckedException(final String defaultMessage, final Throwable cause) {
        super(defaultMessage, cause);
    }

    public SecurityCheckedException(final String defaultMessage) {
        super(defaultMessage);
    }

}
