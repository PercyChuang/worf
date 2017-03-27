package orj.worf.web.base.interceptor;

import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import orj.worf.security.xsrf.Constants;

public class CsrfResponseWrapper extends HttpServletResponseWrapper {

    private static String randomClass = "java.security.SecureRandom";
    private static Random randomSource;
    static {
        try {
            Class<?> clazz = Class.forName(randomClass);
            randomSource = (Random) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public CsrfResponseWrapper(final HttpServletResponse response) {
        super(response);

    }

    @Override
    @Deprecated
    public String encodeRedirectUrl(final String url) {
        return encodeRedirectURL(url);
    }

    @Override
    public String encodeRedirectURL(final String url) {
        return addNonce(super.encodeRedirectURL(url));
    }

    @Override
    @Deprecated
    public String encodeUrl(final String url) {
        return encodeURL(url);
    }

    @Override
    public String encodeURL(final String url) {
        return addNonce(super.encodeURL(url));
    }

    private String addNonce(final String url) {
        if (url == null) {
            return url;
        }
        String path = url;
        String query = "";
        String anchor = "";
        int pound = path.indexOf('#');
        if (pound >= 0) {
            anchor = path.substring(pound);
            path = path.substring(0, pound);
        }
        int question = path.indexOf('?');
        if (question >= 0) {
            query = path.substring(question);
            path = path.substring(0, question);
        }
        StringBuilder sb = new StringBuilder(path);
        if (query.length() > 0) {
            sb.append(query);
            sb.append('&');
        } else {
            sb.append('?');
        }
        sb.append(Constants.TOKEN_REQUEST_PARAM);
        sb.append('=');
        sb.append(generateNonce());
        sb.append(anchor);
        return sb.toString();
    }

    private String generateNonce() {
        byte random[] = new byte[16];
        StringBuilder buffer = new StringBuilder(32);
        randomSource.nextBytes(random);
        for (byte element : random) {
            byte b1 = (byte) ((element & 0xf0) >> 4);
            byte b2 = (byte) (element & 0x0f);
            if (b1 < 10) {
                buffer.append((char) ('0' + b1));
            } else {
                buffer.append((char) ('A' + (b1 - 10)));
            }
            if (b2 < 10) {
                buffer.append((char) ('0' + b2));
            } else {
                buffer.append((char) ('A' + (b2 - 10)));
            }
        }
        return buffer.toString();
    }
}
