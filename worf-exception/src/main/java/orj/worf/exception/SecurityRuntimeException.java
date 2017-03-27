package orj.worf.exception;

public class SecurityRuntimeException extends BaseRuntimeException {

    private static final long serialVersionUID = -1001968495110168387L;

    public SecurityRuntimeException(final String code, final String defaultMessage, final Throwable cause,
            final Object... args) {
        super(code, defaultMessage, cause, args);
    }

    public SecurityRuntimeException(final String code, final String defaultMessage, final Object... args) {
        super(code, defaultMessage, args);
    }

    public SecurityRuntimeException(final String defaultMessage, final Throwable cause) {
        super(defaultMessage, cause);
    }

    public SecurityRuntimeException(final String defaultMessage) {
        super(defaultMessage);
    }

}
