package orj.worf.service.aspect;

import orj.worf.log.annotation.Log;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;

class ArgumentLog extends LoggerStrategy {
    private final Logger logger;
    private final String identifier;
    private final Object[] args;
    private final MethodSignature ms;

    public ArgumentLog(Logger logger, String identifier, Object[] args, MethodSignature ms) {
        this.logger = logger;
        this.identifier = identifier;
        this.args = args;
        this.ms = ms;
    }

    protected void log() {
        Log log = (Log) this.ms.getMethod().getAnnotation(Log.class);
        if (log == null)
            this.logger.info("[{}] Invocation of method '{}' started, args:{}",
                    new Object[] { this.identifier, this.ms.getName(), getArgs(this.args) });
        else
            this.logger.info("[{}] Invocation of method '{}' started, description:{}, args:{}", new Object[] {
                    this.identifier, this.ms.getName(), log.value(), getArgs(this.args) });
    }
}
