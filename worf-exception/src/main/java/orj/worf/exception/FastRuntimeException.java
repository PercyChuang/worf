package orj.worf.exception;

public class FastRuntimeException extends BaseRuntimeException {

    private static final long serialVersionUID = -4954118251735823026L;

    public FastRuntimeException(final String msg) {
        super(msg);
    }

    public FastRuntimeException(final String code, final String msg) {
        super(code, msg);
    }

    public FastRuntimeException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public FastRuntimeException(final String code, final String msg, final Throwable cause) {
        super(code, msg, cause);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
