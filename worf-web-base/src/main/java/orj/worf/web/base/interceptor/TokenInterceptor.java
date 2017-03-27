package orj.worf.web.base.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import orj.worf.web.base.token.TokenHelper;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        if ("POST".equalsIgnoreCase(request.getMethod()) && TokenHelper.validToken(request) == false) {
            response.sendRedirect(request.getContextPath() + "/invalidToken");
            return false;
        }
        return true;
    }

}