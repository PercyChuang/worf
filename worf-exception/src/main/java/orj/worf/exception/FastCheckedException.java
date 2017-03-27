package orj.worf.exception;

public class FastCheckedException extends BaseCheckedException {

    private static final long serialVersionUID = 599320371775054155L;

    public FastCheckedException(final String msg) {
        super(msg);
    }

    public FastCheckedException(final String code, final String msg) {
        super(code, msg);
    }

    public FastCheckedException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public FastCheckedException(final String code, final String msg, final Throwable cause) {
        super(code, msg, cause);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
