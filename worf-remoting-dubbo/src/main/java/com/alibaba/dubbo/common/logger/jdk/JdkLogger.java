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
package com.alibaba.dubbo.common.logger.jdk;

import java.util.logging.Level;

import com.alibaba.dubbo.common.logger.Logger;

public class JdkLogger implements Logger {

    private final java.util.logging.Logger logger;

    public JdkLogger(final java.util.logging.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void trace(final String msg) {
        logger.log(Level.FINER, msg);
    }

    @Override
    public void trace(final Throwable e) {
        logger.log(Level.FINER, e.getMessage(), e);
    }

    @Override
    public void trace(final String msg, final Throwable e) {
        logger.log(Level.FINER, msg, e);
    }

    @Override
    public void debug(final String msg) {
        logger.log(Level.FINE, msg);
    }

    @Override
    public void debug(final Throwable e) {
        logger.log(Level.FINE, e.getMessage(), e);
    }

    @Override
    public void debug(final String msg, final Throwable e) {
        logger.log(Level.FINE, msg, e);
    }

    @Override
    public void info(final String msg) {
        logger.log(Level.INFO, msg);
    }

    @Override
    public void info(final String msg, final Throwable e) {
        logger.log(Level.INFO, msg, e);
    }

    @Override
    public void warn(final String msg) {
        logger.log(Level.WARNING, msg);
    }

    @Override
    public void warn(final String msg, final Throwable e) {
        logger.log(Level.WARNING, msg, e);
    }

    @Override
    public void error(final String msg) {
        logger.log(Level.SEVERE, msg);
    }

    @Override
    public void error(final String msg, final Throwable e) {
        logger.log(Level.SEVERE, msg, e);
    }

    @Override
    public void error(final Throwable e) {
        logger.log(Level.SEVERE, e.getMessage(), e);
    }

    @Override
    public void info(final Throwable e) {
        logger.log(Level.INFO, e.getMessage(), e);
    }

    @Override
    public void warn(final Throwable e) {
        logger.log(Level.WARNING, e.getMessage(), e);
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isLoggable(Level.FINER);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isLoggable(Level.FINE);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isLoggable(Level.INFO);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isLoggable(Level.WARNING);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isLoggable(Level.SEVERE);
    }

}