package orj.worf.service.aspect;

import orj.worf.exception.BaseException;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;

class ExceptionLog extends LoggerStrategy {
    private final Logger logger;
    private final String identifier;
    private final Throwable e;
    private final MethodSignature ms;

    public ExceptionLog(Logger logger, String identifier, Throwable e, MethodSignature ms) {
        this.logger = logger;
        this.identifier = identifier;
        this.e = e;
        this.ms = ms;
    }

    protected void log() {
        String message;
        if (this.e instanceof BaseException)
            message = ((BaseException) this.e).getCode();
        else {
            message = this.e.getMessage();
        }
        this.logger.error("[{}] Invocation of method '{}' threw an exception:{}", new Object[] { this.identifier,
                this.ms.getName(), message });
    }
}