package orj.worf.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.bouncycastle.util.IPAddress;

public final class NetworkUtils {
    private static final String LOCALHOST = "127.0.0.1";
    private static final String ANYHOST = "0.0.0.0";

    private NetworkUtils() {
    }

    /**
     * 取得当前机器的IP地址.
     */
    public static String getLocalAddress() {
        Enumeration<NetworkInterface> interfaces;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (Throwable e) {
            return LOCALHOST;
        }
        if (interfaces != null) {
            while (interfaces.hasMoreElements()) {
                NetworkInterface network = interfaces.nextElement();
                Enumeration<InetAddress> addresses = network.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet6Address) {
                        continue;
                    }
                    if (isValidAddress(address)) {
                        return address.getHostAddress();
                    }
                }
            }
        }
        return LOCALHOST;
    }

    /**
     * 判断IP地址是否合法。
     */
    public static boolean isValidAddress(final InetAddress address) {
        if (address == null || address.isLoopbackAddress()) {
            return false;
        }
        String ip = address.getHostAddress();
        return ip != null && !LOCALHOST.equals(ip) && !ANYHOST.equals(ip) && IPAddress.isValidIPv4(ip);
    }

}
