package orj.worf.service.aspect;

import java.lang.reflect.Method;
import java.util.Properties;
import orj.worf.log.annotation.IgnoreLog;
import orj.worf.util.DataCache;
import orj.worf.util.ObjectId;
import orj.worf.util.PropertiesUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

@Aspect
public class ServiceLogAspect implements Ordered {
    private static final boolean enableAopLog = aopLogEnabled();

    private static final DataCache<String, Logger> LOGGERS = new DataCache("LOGGERS", 86400000L);

    private static boolean aopLogEnabled() {
        Properties properties = PropertiesUtils.loadProperties("application.properties");
        return Boolean.parseBoolean(String.valueOf(properties.get("enableAopLog")));
    }

    @Around("orj.worf.aop.pointcut.SpringPointcuts.springAnnotations() && orj.worf.aop.pointcut.CommonPointcuts.publicMethod()")
    public Object logInboundOutbound(ProceedingJoinPoint jp) throws Throwable {
        if ((!(enableAopLog)) || (gotIgnoreLogOnClassOrMethod(jp))) {
            try {
                return jp.proceed();
            } catch (Throwable e) {
                throw e;
            }
        }
        MethodSignature ms = (MethodSignature) jp.getSignature();
        Logger logger = getLogger(ms);
        String identifier = ObjectId.getId();
        argumentLog(logger, identifier, jp.getArgs(), ms);
        try {
            Object result = jp.proceed();
            resultLog(logger, identifier, result, ms);
            return result;
        } catch (Throwable e) {
            exceptionLog(logger, identifier, e, ms);
            throw e;
        }
    }

    private boolean gotIgnoreLogOnClassOrMethod(ProceedingJoinPoint jp) {
        Class targetClass = jp.getSignature().getDeclaringType();
        if (targetClass.getAnnotation(IgnoreLog.class) != null) {
            return true;
        }
        MethodSignature ms = (MethodSignature) jp.getSignature();
        return (ms.getMethod().getAnnotation(IgnoreLog.class) != null);
    }

    private Logger getLogger(MethodSignature ms) {
        String className = ms.getDeclaringTypeName();
        Logger logger = (Logger) LOGGERS.get(className);
        if (logger == null) {
            logger = LoggerFactory.getLogger(className);
            LOGGERS.put(className, logger);
        }
        return logger;
    }

    private void argumentLog(Logger logger, String identifier, Object[] args, MethodSignature ms) {
        LoggerThreadPool.log(new ArgumentLog(logger, identifier, args, ms));
    }

    private void resultLog(Logger logger, String identifier, Object result, MethodSignature ms) {
        LoggerThreadPool.log(new ResultLog(logger, identifier, result, ms));
    }

    private void exceptionLog(Logger logger, String identifier, Throwable e, MethodSignature ms) {
        LoggerThreadPool.log(new ExceptionLog(logger, identifier, e, ms));
    }

    public int getOrder() {
        return 150;
    }
}
