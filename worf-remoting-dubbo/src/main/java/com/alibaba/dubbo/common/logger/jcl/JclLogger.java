package com.alibaba.dubbo.common.logger.jcl;

import java.io.Serializable;

import org.apache.commons.logging.Log;

import com.alibaba.dubbo.common.logger.Logger;

/**
 * 适配CommonsLogging，依赖于commons-logging.jar <br/> 有关CommonsLogging详细信息请参阅：<a target="_blank"
 * href="http://www.apache.org/">http://www.apache.org/</a>
 *
 * @author liangfei0201@163.com
 *
 */
public class JclLogger implements Logger, Serializable {

    private static final long serialVersionUID = 1L;

    private final Log logger;

    public JclLogger(final Log logger) {
        this.logger = logger;
    }

    @Override
    public void trace(final String msg) {
        logger.trace(msg);
    }

    @Override
    public void trace(final Throwable e) {
        logger.trace(e);
    }

    @Override
    public void trace(final String msg, final Throwable e) {
        logger.trace(msg, e);
    }

    @Override
    public void debug(final String msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(final Throwable e) {
        logger.debug(e);
    }

    @Override
    public void debug(final String msg, final Throwable e) {
        logger.debug(msg, e);
    }

    @Override
    public void info(final String msg) {
        logger.info(msg);
    }

    @Override
    public void info(final Throwable e) {
        logger.info(e);
    }

    @Override
    public void info(final String msg, final Throwable e) {
        logger.info(msg, e);
    }

    @Override
    public void warn(final String msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(final Throwable e) {
        logger.warn(e);
    }

    @Override
    public void warn(final String msg, final Throwable e) {
        logger.warn(msg, e);
    }

    @Override
    public void error(final String msg) {
        logger.error(msg);
    }

    @Override
    public void error(final Throwable e) {
        logger.error(e);
    }

    @Override
    public void error(final String msg, final Throwable e) {
        logger.error(msg, e);
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

}
