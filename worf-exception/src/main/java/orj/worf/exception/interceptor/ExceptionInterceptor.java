package orj.worf.exception.interceptor;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import orj.worf.exception.BaseCheckedException;
import orj.worf.exception.BaseException;
import orj.worf.exception.BaseRuntimeException;
import orj.worf.exception.util.ExceptionUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

class ExceptionInterceptor implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);

    private ApplicationContext applicationContext;

    public void handleAppException(final JoinPoint jp, final Throwable e) throws Throwable {
        final Signature signature = jp.getSignature();
        final String declaringTypeName = signature.getDeclaringTypeName();
        final String methodFullName = declaringTypeName + "." + signature.getName();
        if (logger.isDebugEnabled()) {
            logger.debug("Catch an exception[{}] when invoke method[{}].", e.getClass().getName(), methodFullName);
        }
        Logger log = LoggerFactory.getLogger(declaringTypeName);
        if (ExceptionUtils.isHandledException(e, log)) {
            throw e;
        }
        if (ExceptionUtils.isFastException(e)) {
            log.error("Method [{}] threw an FastException with message {}.", signature.getName(), e.getMessage());
            throw e;
        }
        Throwable exception = validateException(e, methodFullName);
        exception = buildExceptionInfo(jp, exception);
        log(exception);
        throw exception;
    }

    private Throwable validateException(final Throwable e, final String methdoName) throws Throwable {
        Throwable ec = e;
        if (ExceptionUtils.isRuntimeException(e)) {
            if (!(e instanceof BaseRuntimeException)) {
                String code = "EXCEPTION_CODE_01" + System.nanoTime();
                ec = ExceptionUtils.createAppRuntimeException(code, null, e);
            }
        } else if (ExceptionUtils.isCheckedException(e)) {
            if (!(e instanceof BaseCheckedException)) {
                logger.warn(
                        "An exception[{}] has been thrown by the method[{}] is not inherited from BaseCheckedException!",
                        e.getClass().getName(), methdoName);
                String code = "EXCEPTION_CODE_01" + System.nanoTime();
                ec = ExceptionUtils.createAppCheckedException(code, null, e);
            }
        } else {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return ec;
    }

    private Throwable buildExceptionInfo(final JoinPoint jp, final Throwable e) {
        if (e instanceof BaseException) {
            BaseException be = (BaseException) e;
            Signature s = jp.getSignature();
            be.setClassName(s.getDeclaringTypeName());
            be.setMethodName(s.getName());
            String code = be.getCode();
            String i18nMessage;
            if (code != null && code.startsWith("EXCEPTION_CODE_01")) {
                i18nMessage = resolveMessage("EXCEPTION_CODE_01", new Object[] { be.getCode() });
            } else {
                i18nMessage = resolveMessage(code, be.getArgs());
            }
            be.setI18nMessage(StringUtils.isBlank(i18nMessage) ? be.getMessage() : i18nMessage);
            be.setParameters(ExceptionUtils.convertArgsToString(jp.getArgs()));
            be.setTime(new Date());
            be.setHandled(true);
            return (Throwable) be;
        }
        return e;
    }

    private void log(final Throwable e) {
        if (e instanceof BaseException) {
            BaseException be = (BaseException) e;
            Logger log = LoggerFactory.getLogger(be.getClassName());
            log.error(buildLog(e));
        }
    }

    private String buildLog(final Throwable e) {
        BaseException be = (BaseException) e;
        List<String> detail = new LinkedList<String>();
        detail.add("********************** Exception is thrown **********************");
        detail.add(concat("exception.log.code", be.getCode()));
        detail.add(concat("exception.log.classname", be.getClassName()));
        detail.add(concat("exception.log.methodname", be.getMethodName()));
        detail.add(concat("exception.log.parameters", Arrays.asList(be.getParameters())));
        detail.add(concat("exception.log.i18nMessage", be.getI18nMessage()));
        detail.add(concat("exception.log.time",
                DateFormatUtils.format(be.getTime(), "yyyy-MM-dd HH:mm:ss", Locale.getDefault())));
        detail.add(concat("exception.log.stacktrace", "")
                + org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
        return appendWithOSLineSeparator(detail);
    }

    private String concat(final String key, final Object obj) {
        StringBuilder buf = new StringBuilder(32);
        buf.append(resolveMessage(key, null));
        buf.append(" : ");
        buf.append(obj);
        return buf.toString();
    }

    private String resolveMessage(final String code, final Object[] args) {
        try {
            String message = this.applicationContext.getMessage(code, args, Locale.getDefault());
            if (logger.isDebugEnabled()) {
                logger.debug("The i18n key=[{}], message = [{}]", code, message);
            }
            return message;
        } catch (Exception ignore) {
            return StringUtils.EMPTY;
        }
    }

    private String appendWithOSLineSeparator(final List<String> str) {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        for (String s : str) {
            sb.append(s);
            sb.append(lineSeparator);
        }
        return sb.toString();
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}