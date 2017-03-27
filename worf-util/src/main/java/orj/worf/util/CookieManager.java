package orj.worf.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class CookieManager {
    private static final String SCHEME_SEPARATOR = "://";
    private static final CookieManager cookieManager = new CookieManager();
    private ConcurrentMap<String, Map<String, String>> cookies = new ConcurrentHashMap<String, Map<String, String>>();
    private ConcurrentMap<String, String> urls = new ConcurrentHashMap<String, String>();

    private CookieManager() {
    }

    public static CookieManager instance() {
        return cookieManager;
    }

    public String getCookies(final String url) {
        Map<String, String> urlCookies = cookies.get(getServerHost(url));
        if (urlCookies != null) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> cookieEntry : urlCookies.entrySet()) {
                if (sb.length() > 0) {
                    sb.append("; ");
                }
                sb.append(cookieEntry.getKey()).append("=").append(cookieEntry.getValue());
            }
            return sb.toString();
        }
        return "";
    }

    public void setCookies(final String url, final String cookieStr) {
        String serverHost = getServerHost(url);
        Map<String, String> urlCookies = cookies.get(serverHost);
        if (urlCookies == null) {
            urlCookies = new ConcurrentHashMap<String, String>();
            cookies.putIfAbsent(serverHost, urlCookies);
        }
        String[] cookies = cookieStr.split(";");
        for (String cookie : cookies) {
            if (cookie != null && cookie.indexOf("=") > 0) {
                cookie = cookie.trim();
                urlCookies.put(cookie.split("=")[0].trim(), cookie.split("=")[1].trim());
            }
        }
    }

    public void removeCookies(final String url) {
        cookies.remove(getServerHost(url));
    }

    private String getServerHost(final String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }
        if (!urls.containsKey(url)) {
            String host = url;
            int index = host.indexOf(SCHEME_SEPARATOR);
            if (index > -1) {
                host = host.substring(index + SCHEME_SEPARATOR.length());
            }
            index = host.indexOf("/");
            if (index > -1) {
                host = host.substring(0, index);
            }
            urls.putIfAbsent(url, host);
        }
        return urls.get(url);
    }
}
