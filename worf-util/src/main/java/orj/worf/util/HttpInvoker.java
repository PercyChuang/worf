package orj.worf.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpInvoker {

    private static CookieManager cookieManager = CookieManager.instance();

    private static enum Method {
        POST, GET;
    }

    private HttpInvoker() {
    }

    public static String get(final String url) throws IOException {
        ResourceTracker tracker = new ResourceTracker("GET");
        HttpURLConnection conn = getConnection(url, Method.GET);
        try {
            List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
            if (cookies != null) {
                for (String cookie : cookies) {
                    cookieManager.setCookies(url, cookie);
                }
            }
            return handleResponse(tracker, conn);
        } finally {
            tracker.clear();
            conn.disconnect();
        }
    }

    public static String post(final String url, final String parameter) throws IOException {
        ResourceTracker tracker = new ResourceTracker("POST");
        HttpURLConnection conn = getConnection(url, Method.POST);
        try {
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            tracker.attach(out);
            out.writeBytes(parameter == null ? "" : parameter);
            out.flush();
            return handleResponse(tracker, conn);
        } finally {
            tracker.clear();
            conn.disconnect();
        }
    }

    public static String post(final String url, final Map<String, String> parameter) throws IOException {
        ResourceTracker tracker = new ResourceTracker("POST");
        HttpURLConnection conn = getConnection(url, Method.POST);
        try {
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            tracker.attach(out);
            out.writeBytes(createLinkString(parameter));
            out.flush();
            return handleResponse(tracker, conn);
        } finally {
            tracker.clear();
            conn.disconnect();
        }
    }

    private static HttpURLConnection getConnection(final String url, final Method method) throws IOException {
        URLConnection urlConnection = new URL(url).openConnection();
        HttpURLConnection conn = (HttpURLConnection) urlConnection;
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);
        conn.setUseCaches(false);
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 Chrome/26.0.1410.43 Safari/535.12");
        conn.setRequestProperty("Cookie", cookieManager.getCookies(url));
        if (Method.POST == method) {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
        } else {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
        }
        conn.connect();
        return conn;
    }

    private static String createLinkString(final Map<String, String> params) throws IOException {
        Map<String, String> parameter = paramsFilter(params);
        List<String> keys = new ArrayList<String>(parameter.keySet());
        Collections.sort(keys);
        StringBuilder builder = new StringBuilder(128);
        for (int i = 0; i < keys.size(); i++) {
            if (i > 0) {
                builder.append("&");
            }
            String key = keys.get(i);
            builder.append(key).append("=").append(parameter.get(key));
        }
        return builder.toString();
    }

    private static Map<String, String> paramsFilter(final Map<String, String> parameters) throws IOException {
        Map<String, String> result = new ConcurrentHashMap<String, String>();
        if (parameters == null || parameters.size() <= 0) {
            return result;
        }
        for (String key : parameters.keySet()) {
            String value = parameters.get(key);
            if (value == null || "".equals(value) || key == null || "".equals(key)) {
                continue;
            }
            result.put(key, URLEncoder.encode(value, "UTF-8"));
        }
        return result;
    }

    private static String handleResponse(final ResourceTracker tracker, final HttpURLConnection conn)
            throws IOException {
        String contentType = conn.getHeaderField("Content-Type");
        String charset = null;
        if (contentType != null) {
            for (String param : contentType.replace(" ", "").split(";")) {
                if (param.startsWith("charset=")) {
                    charset = param.split("=", 2)[1];
                    break;
                }
            }
        }
        StringBuilder builder = new StringBuilder(128);
        BufferedReader reader;
        if (charset != null) {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
        } else {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        }
        tracker.attach(reader);
        for (String line; (line = reader.readLine()) != null;) {
            builder.append(line);
        }
        return builder.toString();
    }

}
