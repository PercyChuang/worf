package orj.worf.exception.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import orj.worf.exception.AppCheckedException;
import orj.worf.exception.AppRuntimeException;
import orj.worf.exception.BaseException;
import orj.worf.exception.FastCheckedException;
import orj.worf.exception.FastRuntimeException;
import orj.worf.exception.config.ExceptionConfigHelper;

import org.slf4j.Logger;

public class ExceptionUtils extends org.apache.commons.lang3.exception.ExceptionUtils {

    public static boolean isFastException(final Throwable e) {
        return FastCheckedException.class.isInstance(e) || FastRuntimeException.class.isInstance(e);
    }

    public static boolean isCheckedException(final Throwable e) {
        return e instanceof Exception && !isRuntimeException(e);
    }

    public static boolean isRuntimeException(final Throwable e) {
        return e instanceof RuntimeException;
    }

    public static boolean isHandledException(final Throwable e, final Logger logger) {
        List<BaseException> bes = findAllBaseException(e);
        for (BaseException be : bes) {
            if (be.isHandled()) {
                logger.error("An exception occurs again, find the code[{}] in the log file for details.", be.getCode());
                return true;
            }
        }
        return false;
    }

    private static List<BaseException> findAllBaseException(final Throwable e) {
        List<BaseException> bes = new LinkedList<BaseException>();
        Throwable[] throwables = getThrowables(e);
        for (Throwable t : throwables) {
            if (t instanceof BaseException) {
                bes.add((BaseException) t);
            }
        }
        return bes;
    }

    public static void throwAppRuntimeException(final String code, final String defaultMessage, final Object... args) {
        throw createAppRuntimeException(code, defaultMessage, null, args);
    }

    public static void throwAppRuntimeException(final String code, final String defaultMessage, final Throwable cause,
            final Object... args) {
        throw createAppRuntimeException(code, defaultMessage, cause, args);
    }

    public static void throwAppCheckedException(final String code, final String defaultMessage, final Object... args)
            throws AppCheckedException {
        throw createAppCheckedException(code, defaultMessage, null, args);
    }

    public static void throwAppCheckedException(final String code, final String defaultMessage, final Throwable cause,
            final Object... args) throws AppCheckedException {
        throw createAppCheckedException(code, defaultMessage, cause, args);
    }

    public static AppRuntimeException createAppRuntimeException(final String code, final String defaultMessage,
            final Throwable cause, final Object... args) {
        if (cause == null) {
            return new AppRuntimeException(code, defaultMessage, args);
        }
        return new AppRuntimeException(code, defaultMessage, cause, args);
    }

    public static AppCheckedException createAppCheckedException(final String code, final String defaultMessage,
            final Throwable cause, final Object... args) {
        if (cause == null) {
            return new AppCheckedException(code, defaultMessage, args);
        }
        return new AppCheckedException(code, defaultMessage, cause, args);
    }

    public static String[] convertArgsToString(final Object[] args) {
        String[] argsStrs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            argsStrs[i] = String.valueOf(args[i]);
        }
        return argsStrs;
    }

    public static String toString(final Throwable e) {
        return toString("", e);
    }

    public static String toString(final String msg, final Throwable e) {
        StringWriter w = new StringWriter();
        w.write(msg);
        PrintWriter p = new PrintWriter(w);
        p.println();
        try {
            e.printStackTrace(p);
            return w.toString();
        } finally {
            p.close();
        }
    }

    @Deprecated
    public static AppRuntimeException olderCreateAppRuntimeException(final String code, final String defaultMessage,
            final Throwable cause, final Object... params) {
        String codePrefix = code.substring(0, code.lastIndexOf('_') + 1);
        Class<? extends BaseException> exClazz = ExceptionConfigHelper.getExeptionClazzMapping().get(codePrefix);
        if (exClazz == null) {
            throw new RuntimeException("illegal codePrefix[" + codePrefix + "]");
        }
        if (exClazz == AppRuntimeException.class) {
            if (cause == null) {
                return new AppRuntimeException(code, defaultMessage, params);
            }
            return new AppRuntimeException(code, defaultMessage, cause, params);
        }
        Class<?>[] throwableArgsClass = null;
        Object[] throwableArgs = null;
        if (cause == null) {
            throwableArgsClass = new Class[] { String.class, String.class, Object[].class };
            throwableArgs = new Object[] { code, defaultMessage, params };
        } else {
            throwableArgsClass = new Class[] { String.class, String.class, Throwable.class, Object[].class };
            throwableArgs = new Object[] { code, defaultMessage, cause, params };
        }
        try {
            Constructor<? extends BaseException> exConstructor = exClazz.getConstructor(throwableArgsClass);
            return (AppRuntimeException) exConstructor.newInstance(throwableArgs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
