package orj.worf.exception;

import java.util.Date;

import orj.worf.exception.util.ExceptionUtils;

import org.springframework.core.NestedRuntimeException;

public class BaseRuntimeException extends NestedRuntimeException implements BaseException {
    private static final long serialVersionUID = -5889040288739122793L;
    private String code;
    private Date time;
    private String[] args;
    private String className;
    private String methodName;
    private String[] parameters;
    private boolean handled;
    private String i18nMessage;

    public BaseRuntimeException(final String code, final String defaultMessage, final Object... args) {
        super(defaultMessage);
        this.code = code;
        this.args = ExceptionUtils.convertArgsToString(args);
    }

    public BaseRuntimeException(final String code, final String defaultMessage, final Throwable cause,
            final Object... args) {
        super(defaultMessage, cause);
        this.code = code;
        this.args = ExceptionUtils.convertArgsToString(args);
    }

    public BaseRuntimeException(final String defaultMessage, final Throwable cause) {
        super(defaultMessage, cause);
    }

    public BaseRuntimeException(final String defaultMessage) {
        super(defaultMessage);
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public Date getTime() {
        return this.time;
    }

    @Override
    public void setTime(final Date time) {
        this.time = time;
    }

    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public void setClassName(final String className) {
        this.className = className;
    }

    @Override
    public String getMethodName() {
        return this.methodName;
    }

    @Override
    public void setMethodName(final String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String[] getParameters() {
        return this.parameters;
    }

    @Override
    public void setParameters(final String[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public void setHandled(final boolean handled) {
        this.handled = handled;
    }

    @Override
    public boolean isHandled() {
        return this.handled;
    }

    @Override
    public void setI18nMessage(final String i18nMessage) {
        this.i18nMessage = i18nMessage;
    }

    @Override
    public String getI18nMessage() {
        return i18nMessage;
    }

    @Override
    public String[] getArgs() {
        return args;
    }
}
