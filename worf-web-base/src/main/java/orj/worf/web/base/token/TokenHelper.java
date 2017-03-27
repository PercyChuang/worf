package orj.worf.web.base.token;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TokenHelper
 */
public class TokenHelper {

    private static final Logger logger = LoggerFactory.getLogger(TokenHelper.class);
    public static final String TOKEN_NAME_FIELD = "ooh.token.name";
    public static final String TOKEN_VALUE_FIELD = "ooh.token.value";

    public static Token setToken(final HttpServletRequest request) {
        Token token = Token.build();
        HttpSession session = request.getSession();
        try {
            session.setAttribute(token.getName(), token.getValue());
            if (logger.isDebugEnabled()) {
                logger.debug("请求[{}:{}]产生的Token：{}", uri(request), sesssionId(request), token);
            }
        } catch (IllegalStateException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalArgumentException(e.getMessage());
        }
        return token;
    }

    private static String getTokenName(final HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        if (!params.containsKey(TOKEN_NAME_FIELD)) {
            return null;
        }
        String[] tokenNames = params.get(TOKEN_NAME_FIELD);
        if (tokenNames == null || tokenNames.length < 1) {
            return null;
        }
        return tokenNames[0];
    }

    private static String getTokenValue(final HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        if (!params.containsKey(TOKEN_VALUE_FIELD)) {
            return null;
        }
        String[] tokenValues = params.get(TOKEN_VALUE_FIELD);
        if (tokenValues == null || tokenValues.length < 1) {
            return null;
        }
        return tokenValues[0];
    }

    public static boolean validToken(final HttpServletRequest request) {
        String tokenName = getTokenName(request);
        if (logger.isDebugEnabled()) {
            logger.debug("请求[{}:{}]提交的tokenName：{}", uri(request), sesssionId(request), tokenName);
        }
        if (StringUtils.isBlank(tokenName)) {
            return false;
        }
        String tokenValue = getTokenValue(request);
        if (logger.isDebugEnabled()) {
            logger.debug("请求[{}:{}]提交的tokenValue：{}", uri(request), sesssionId(request), tokenValue);
        }
        if (StringUtils.isBlank(tokenValue)) {
            return false;
        }
        HttpSession session = request.getSession(false);
        synchronized (session) {
            String sessionToken = (String) session.getAttribute(tokenName);
            if (logger.isDebugEnabled()) {
                logger.debug("请求[{}:{}]缓存的token为:{}", uri(request), sesssionId(request), sessionToken);
            }
            if (!tokenValue.equals(sessionToken)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("请求[{}:{}]提交的token验证失败", uri(request), sesssionId(request));
                }
                return false;
            }
            session.removeAttribute(tokenName);
            if (logger.isDebugEnabled()) {
                logger.debug("请求[{}:{}]提交的token验证通过", uri(request), sesssionId(request));
            }
        }
        return true;
    }

    private static String sesssionId(final HttpServletRequest request) {
        return request.getSession().getId();
    }

    private static String uri(final HttpServletRequest request) {
        return request.getRequestURI();
    }

}