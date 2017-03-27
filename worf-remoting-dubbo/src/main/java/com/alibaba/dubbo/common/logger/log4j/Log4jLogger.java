/*
 * Copyright 1999-2011 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.common.logger.log4j;

import org.apache.log4j.Level;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.support.FailsafeLogger;

public class Log4jLogger implements Logger {

    private static final String FQCN = FailsafeLogger.class.getName();

    private final org.apache.log4j.Logger logger;

    public Log4jLogger(final org.apache.log4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void trace(final String msg) {
        logger.log(FQCN, Level.TRACE, msg, null);
    }

    @Override
    public void trace(final Throwable e) {
        logger.log(FQCN, Level.TRACE, e == null ? null : e.getMessage(), e);
    }

    @Override
    public void trace(final String msg, final Throwable e) {
        logger.log(FQCN, Level.TRACE, msg, e);
    }

    @Override
    public void debug(final String msg) {
        logger.log(FQCN, Level.DEBUG, msg, null);
    }

    @Override
    public void debug(final Throwable e) {
        logger.log(FQCN, Level.DEBUG, e == null ? null : e.getMessage(), e);
    }

    @Override
    public void debug(final String msg, final Throwable e) {
        logger.log(FQCN, Level.DEBUG, msg, e);
    }

    @Override
    public void info(final String msg) {
        logger.log(FQCN, Level.INFO, msg, null);
    }

    @Override
    public void info(final Throwable e) {
        logger.log(FQCN, Level.INFO, e == null ? null : e.getMessage(), e);
    }

    @Override
    public void info(final String msg, final Throwable e) {
        logger.log(FQCN, Level.INFO, msg, e);
    }

    @Override
    public void warn(final String msg) {
        logger.log(FQCN, Level.WARN, msg, null);
    }

    @Override
    public void warn(final Throwable e) {
        logger.log(FQCN, Level.WARN, e == null ? null : e.getMessage(), e);
    }

    @Override
    public void warn(final String msg, final Throwable e) {
        logger.log(FQCN, Level.WARN, msg, e);
    }

    @Override
    public void error(final String msg) {
        logger.log(FQCN, Level.ERROR, msg, null);
    }

    @Override
    public void error(final Throwable e) {
        logger.log(FQCN, Level.ERROR, e == null ? null : e.getMessage(), e);
    }

    @Override
    public void error(final String msg, final Throwable e) {
        logger.log(FQCN, Level.ERROR, msg, e);
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
        return logger.isEnabledFor(Level.WARN);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isEnabledFor(Level.ERROR);
    }

}