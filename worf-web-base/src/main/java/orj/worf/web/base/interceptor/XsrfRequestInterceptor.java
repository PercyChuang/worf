package orj.worf.web.base.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import orj.worf.security.xsrf.CsrfTokenHelper;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class XsrfRequestInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        try {
            CsrfTokenHelper.validateToken(request);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/unauthorized");
            return false;
        }
        return true;
    }

}