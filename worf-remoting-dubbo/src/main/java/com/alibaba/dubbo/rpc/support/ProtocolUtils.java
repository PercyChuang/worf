package com.alibaba.dubbo.rpc.support;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;

/**
 * @author <a href="mailto:gang.lvg@alibaba-inc.com">kimi</a>
 */
public class ProtocolUtils {

    private ProtocolUtils() {
    }

    public static String serviceKey(final URL url) {
        return serviceKey(url.getPort(), url.getPath(), url.getParameter(Constants.VERSION_KEY),
                url.getParameter(Constants.GROUP_KEY));
    }

    public static String serviceKey(final int port, final String serviceName, final String serviceVersion,
            final String serviceGroup) {
        StringBuilder buf = new StringBuilder();
        if (serviceGroup != null && serviceGroup.length() > 0) {
            buf.append(serviceGroup);
            buf.append("/");
        }
        buf.append(serviceName);
        if (serviceVersion != null && serviceVersion.length() > 0 && !"0.0.0".equals(serviceVersion)) {
            buf.append(":");
            buf.append(serviceVersion);
        }
        buf.append(":");
        buf.append(port);
        return buf.toString();
    }

    public static boolean isGeneric(final String generic) {
        return generic != null && !"".equals(generic)
                && (Constants.GENERIC_SERIALIZATION_DEFAULT.equalsIgnoreCase(generic) /* 正常的泛化调用 */
                || Constants.GENERIC_SERIALIZATION_NATIVE_JAVA.equalsIgnoreCase(generic)); /* 支持java序列化的流式泛化调用 */
    }

    public static boolean isDefaultGenericSerialization(final String generic) {
        return isGeneric(generic) && Constants.GENERIC_SERIALIZATION_DEFAULT.equalsIgnoreCase(generic);
    }

    public static boolean isJavaGenericSerialization(final String generic) {
        return isGeneric(generic) && Constants.GENERIC_SERIALIZATION_NATIVE_JAVA.equalsIgnoreCase(generic);
    }
}
