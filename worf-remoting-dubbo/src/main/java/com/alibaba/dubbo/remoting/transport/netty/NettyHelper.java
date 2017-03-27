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
package com.alibaba.dubbo.remoting.transport.netty;

import org.jboss.netty.logging.AbstractInternalLogger;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

/**
 * @author <a href="mailto:gang.lvg@taobao.com">kimi</a>
 */
final class NettyHelper {

    public static void setNettyLoggerFactory() {
        InternalLoggerFactory factory = InternalLoggerFactory.getDefaultFactory();
        if (factory == null || !(factory instanceof DubboLoggerFactory)) {
            InternalLoggerFactory.setDefaultFactory(new DubboLoggerFactory());
        }
    }

    static class DubboLoggerFactory extends InternalLoggerFactory {

        @Override
        public InternalLogger newInstance(final String name) {
            return new DubboLogger(LoggerFactory.getLogger(name));
        }
    }

    static class DubboLogger extends AbstractInternalLogger {

        private Logger logger;

        DubboLogger(final Logger logger) {
            this.logger = logger;
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

        @Override
        public void debug(final String msg) {
            logger.debug(msg);
        }

        @Override
        public void debug(final String msg, final Throwable cause) {
            logger.debug(msg, cause);
        }

        @Override
        public void info(final String msg) {
            logger.info(msg);
        }

        @Override
        public void info(final String msg, final Throwable cause) {
            logger.info(msg, cause);
        }

        @Override
        public void warn(final String msg) {
            logger.warn(msg);
        }

        @Override
        public void warn(final String msg, final Throwable cause) {
            logger.warn(msg, cause);
        }

        @Override
        public void error(final String msg) {
            logger.error(msg);
        }

        @Override
        public void error(final String msg, final Throwable cause) {
            logger.error(msg, cause);
        }

        @Override
        public String toString() {
            return logger.toString();
        }
    }

}
