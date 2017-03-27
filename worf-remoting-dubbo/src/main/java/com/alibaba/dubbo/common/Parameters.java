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
package com.alibaba.dubbo.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;

/**
 * 兼容2.0.5之前版本
 * 
 * @deprecated
 * @author tony.chenl
 */
@Deprecated
public class Parameters {
    private final Map<String, String> parameters;

    protected static final Logger logger = LoggerFactory.getLogger(Parameters.class);

    public Parameters(final String... pairs) {
        this(toMap(pairs));
    }

    public Parameters(final Map<String, String> parameters) {
        this.parameters = Collections.unmodifiableMap(parameters != null ? new HashMap<String, String>(parameters)
                : new HashMap<String, String>(0));
    }

    private static Map<String, String> toMap(final String... pairs) {
        Map<String, String> parameters = new HashMap<String, String>();
        if (pairs.length > 0) {
            if (pairs.length % 2 != 0) {
                throw new IllegalArgumentException("pairs must be even.");
            }
            for (int i = 0; i < pairs.length; i = i + 2) {
                parameters.put(pairs[i], pairs[i + 1]);
            }
        }
        return parameters;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public <T> T getExtension(final Class<T> type, final String key) {
        String name = getParameter(key);
        return ExtensionLoader.getExtensionLoader(type).getExtension(name);
    }

    public <T> T getExtension(final Class<T> type, final String key, final String defaultValue) {
        String name = getParameter(key, defaultValue);
        return ExtensionLoader.getExtensionLoader(type).getExtension(name);
    }

    public <T> T getMethodExtension(final Class<T> type, final String method, final String key) {
        String name = getMethodParameter(method, key);
        return ExtensionLoader.getExtensionLoader(type).getExtension(name);
    }

    public <T> T getMethodExtension(final Class<T> type, final String method, final String key,
            final String defaultValue) {
        String name = getMethodParameter(method, key, defaultValue);
        return ExtensionLoader.getExtensionLoader(type).getExtension(name);
    }

    public String getDecodedParameter(final String key) {
        return getDecodedParameter(key, null);
    }

    public String getDecodedParameter(final String key, final String defaultValue) {
        String value = getParameter(key, defaultValue);
        if (value != null && value.length() > 0) {
            try {
                value = URLDecoder.decode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return value;
    }

    public String getParameter(final String key) {
        String value = parameters.get(key);
        if (value == null || value.length() == 0) {
            value = parameters.get(Constants.HIDE_KEY_PREFIX + key);
        }
        if (value == null || value.length() == 0) {
            value = parameters.get(Constants.DEFAULT_KEY_PREFIX + key);
        }
        if (value == null || value.length() == 0) {
            value = parameters.get(Constants.HIDE_KEY_PREFIX + Constants.DEFAULT_KEY_PREFIX + key);
        }
        return value;
    }

    public String getParameter(final String key, final String defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }

    public int getIntParameter(final String key) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    public int getIntParameter(final String key, final int defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public int getPositiveIntParameter(final String key, final int defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        int i = Integer.parseInt(value);
        if (i > 0) {
            return i;
        }
        return defaultValue;
    }

    public boolean getBooleanParameter(final String key) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    public boolean getBooleanParameter(final String key, final boolean defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public boolean hasParamter(final String key) {
        String value = getParameter(key);
        return value != null && value.length() > 0;
    }

    public String getMethodParameter(final String method, final String key) {
        String value = parameters.get(method + "." + key);
        if (value == null || value.length() == 0) {
            value = parameters.get(Constants.HIDE_KEY_PREFIX + method + "." + key);
        }
        if (value == null || value.length() == 0) {
            return getParameter(key);
        }
        return value;
    }

    public String getMethodParameter(final String method, final String key, final String defaultValue) {
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }

    public int getMethodIntParameter(final String method, final String key) {
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    public int getMethodIntParameter(final String method, final String key, final int defaultValue) {
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public int getMethodPositiveIntParameter(final String method, final String key, final int defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        int i = Integer.parseInt(value);
        if (i > 0) {
            return i;
        }
        return defaultValue;
    }

    public boolean getMethodBooleanParameter(final String method, final String key) {
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    public boolean getMethodBooleanParameter(final String method, final String key, final boolean defaultValue) {
        String value = getMethodParameter(method, key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public boolean hasMethodParamter(final String method, final String key) {
        String value = getMethodParameter(method, key);
        return value != null && value.length() > 0;
    }

    public static Parameters parseParameters(final String query) {
        return new Parameters(StringUtils.parseQueryString(query));
    }

    @Override
    public boolean equals(final Object o) {
        return parameters.equals(o);
    }

    @Override
    public int hashCode() {
        return parameters.hashCode();
    }

    @Override
    public String toString() {
        return StringUtils.toQueryString(getParameters());
    }

}