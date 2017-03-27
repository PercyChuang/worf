package com.alibaba.dubbo.common.logger.slf4j;

import java.io.Serializable;

import com.alibaba.dubbo.common.logger.Logger;

public class Slf4jLogger implements Logger, Serializable {

    private static final long serialVersionUID = 1L;

    private final org.slf4j.Logger logger;

    public Slf4jLogger(final org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void trace(final String msg) {
        logger.trace(msg);
    }

    @Override
    public void trace(final Throwable e) {
        logger.trace(e.getMessage(), e);
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
        logger.debug(e.getMessage(), e);
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
        logger.info(e.getMessage(), e);
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
        logger.warn(e.getMessage(), e);
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
        logger.error(e.getMessage(), e);
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
