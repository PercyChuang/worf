package orj.worf.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieUtils {
    private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    public static String getCookie(final HttpServletRequest request, final String name) {
        Cookie cookies[] = request.getCookies();
        if (cookies == null || cookies.length == 0 || name == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static void setCookie(final HttpServletResponse response, final String name, final String value) {
        setCookie(response, name, value, -1);
    }

    public static void setCookie(final HttpServletResponse response, final String name, final String value,
            final int maxAge) {
        doSetCookie(response, name, value, "/", maxAge);
    }

    public static void setCookie(final HttpServletResponse response, final String name, final String value,
            final String path) {
        doSetCookie(response, name, value, path, -1);
    }

    public static void setCookie(final HttpServletResponse response, final String name, final String value,
            final String path, final int maxAge) {
        doSetCookie(response, name, value, path, maxAge);
    }

    public static void deleteCookie(final HttpServletResponse response, final String name) {
        doSetCookie(response, name, "", "/", -1);
    }

    public static void deleteCookie(final HttpServletResponse response, final String name, final String path) {
        doSetCookie(response, name, "", path, -1);
    }

    private static final void doSetCookie(final HttpServletResponse response, final String name, final String value,
            final String path, final int maxAge) {
        try {
            Cookie cookie = new Cookie(name, value);
            cookie.setMaxAge(maxAge);
            cookie.setHttpOnly(true);
            cookie.setPath(path);
            response.addCookie(cookie);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
