package orj.worf.security.xsrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import orj.worf.security.crypto.Digester;
import orj.worf.security.exception.CsrfTokenException;
import orj.worf.util.CookieUtils;
import orj.worf.util.Hex;
import orj.worf.util.HttpUtils;
import orj.worf.util.ObjectId;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class CsrfTokenHelper {
    private static final XLogger logger = XLoggerFactory.getXLogger(CsrfTokenHelper.class);

    public static void setupToken(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String cookieToken = ObjectId.getIdentityId();
            session.setAttribute("COOKIE_CSRF_TOKEN", cookieToken);
            CookieUtils.setCookie(response, "xsrf.token", cookieToken);
            session.setAttribute("USER_AGENT_HASH", hashUserAgent(request, session));
            if (logger.isDebugEnabled())
                logger.debug("请求[{}]设置Csrf相关配置完成.", session.getId());
        }
    }

    private static String hashUserAgent(HttpServletRequest request, HttpSession session) {
        String userAgent = request.getHeader("User-Agent");
        if (logger.isDebugEnabled()) {
            logger.debug("请求[{}]对应的User-Agent[{}]及Ip[{}]",
                    new Object[] { session.getId(), userAgent, HttpUtils.getRemoteIpAddr(request) });
        }

        String userAgentMics = userAgent + " {" + session.getId() + "}";
        Digester digester = new Digester("SHA-1", 1);
        return Hex.encodeHexString(digester.digest(userAgentMics.getBytes()));
    }

    public static void removeToken(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        session.removeAttribute("COOKIE_CSRF_TOKEN");
        session.removeAttribute("USER_AGENT_HASH");
        CookieUtils.deleteCookie(response, "xsrf.token");
        if (logger.isDebugEnabled())
            logger.debug("请求[{}]移除CsrfToken完成.", session.getId());
    }

    public static void validateToken(HttpServletRequest request) throws CsrfTokenException {
        HttpSession session = request.getSession(false);
        if (session != null)
            synchronized (session) {
                validateCookie(request, session);
                validateUserAgentHash(request, session);
            }
        else
            throw new CsrfTokenException("Session is null");
    }

    private static void validateCookie(HttpServletRequest request, HttpSession session) throws CsrfTokenException {
        String sessionToken = (String) session.getAttribute("COOKIE_CSRF_TOKEN");
        if (StringUtils.isNotBlank(sessionToken)) {
            String cookie = CookieUtils.getCookie(request, "xsrf.token");
            if ((StringUtils.isNotBlank(cookie)) && (!(sessionToken.equals(cookie))))
                throw new CsrfTokenException("Cookie[xsrf.token]不匹配");
        } else {
            logger.warn("验证Cookie[xsrf.token]时未发现Session[{}]中有对应的数据.", session.getId());
        }
    }

    private static void validateUserAgentHash(HttpServletRequest request, HttpSession session)
            throws CsrfTokenException {
        String saved = (String) session.getAttribute("USER_AGENT_HASH");
        if (StringUtils.isNotBlank(saved)) {
            String current = hashUserAgent(request, session);
            if (!(saved.equals(current)))
                throw new CsrfTokenException("当前用户User-Agent与登录时保存的不一致");
        } else {
            logger.warn("验证[User-Agent]时未发现Session[{}]中有对应的数据.", session.getId());
        }
    }
}