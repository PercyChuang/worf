package orj.worf.service.aspect;

import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;

class ResultLog extends LoggerStrategy {
    private final Logger logger;
    private final String identifier;
    private final Object result;
    private final MethodSignature ms;

    public ResultLog(Logger logger, String identifier, Object result, MethodSignature ms) {
        this.logger = logger;
        this.identifier = identifier;
        this.result = result;
        this.ms = ms;
    }

    protected void log() {
        if (Void.TYPE == this.ms.getReturnType())
            this.logger.info("[{}] Invocation of method(V) '{}' completed.", this.identifier, this.ms.getName());
        else
            this.logger.info("[{}] Invocation of method '{}' completed, result:{}", new Object[] { this.identifier,
                    this.ms.getName(), reflection(this.result) });
    }
}
