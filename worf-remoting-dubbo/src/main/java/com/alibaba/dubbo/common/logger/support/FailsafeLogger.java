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
package com.alibaba.dubbo.common.logger.support;

import com.alibaba.dubbo.common.Version;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.utils.NetUtils;

public class FailsafeLogger implements Logger {

    private Logger logger;

    public FailsafeLogger(final Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(final Logger logger) {
        this.logger = logger;
    }

    private String appendContextMessage(final String msg) {
        return " [DUBBO] " + msg + ", dubbo version: " + Version.getVersion() + ", current host: "
                + NetUtils.getLogHost();
    }

    @Override
    public void trace(final String msg, final Throwable e) {
        try {
            logger.trace(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void trace(final Throwable e) {
        try {
            logger.trace(e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void trace(final String msg) {
        try {
            logger.trace(appendContextMessage(msg));
        } catch (Throwable t) {
        }
    }

    @Override
    public void debug(final String msg, final Throwable e) {
        try {
            logger.debug(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void debug(final Throwable e) {
        try {
            logger.debug(e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void debug(final String msg) {
        try {
            logger.debug(appendContextMessage(msg));
        } catch (Throwable t) {
        }
    }

    @Override
    public void info(final String msg, final Throwable e) {
        try {
            logger.info(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void info(final String msg) {
        try {
            logger.info(appendContextMessage(msg));
        } catch (Throwable t) {
        }
    }

    @Override
    public void warn(final String msg, final Throwable e) {
        try {
            logger.warn(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void warn(final String msg) {
        try {
            logger.warn(appendContextMessage(msg));
        } catch (Throwable t) {
        }
    }

    @Override
    public void error(final String msg, final Throwable e) {
        try {
            logger.error(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void error(final String msg) {
        try {
            logger.error(appendContextMessage(msg));
        } catch (Throwable t) {
        }
    }

    @Override
    public void error(final Throwable e) {
        try {
            logger.error(e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void info(final Throwable e) {
        try {
            logger.info(e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void warn(final Throwable e) {
        try {
            logger.warn(e);
        } catch (Throwable t) {
        }
    }

    @Override
    public boolean isTraceEnabled() {
        try {
            return logger.isTraceEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public boolean isDebugEnabled() {
        try {
            return logger.isDebugEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public boolean isInfoEnabled() {
        try {
            return logger.isInfoEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public boolean isWarnEnabled() {
        try {
            return logger.isWarnEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public boolean isErrorEnabled() {
        try {
            return logger.isErrorEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

}