package orj.worf.web.base.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import orj.worf.util.HttpUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.HtmlUtils;

public class ParameterInterceptor extends HandlerInterceptorAdapter {

    private static final XLogger logger = XLoggerFactory.getXLogger(ParameterInterceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        HttpServletRequest req = request;
        if (request instanceof EscapedRequestWrapper) {
            req = ((EscapedRequestWrapper) request).getOriginalRequest();
        }
        String requestURI = req.getRequestURI();
        Enumeration<String> names = req.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String[] values = req.getParameterValues(name);
            if (values == null || values.length == 0) {
                continue;
            }
            for (String value : values) {
                String v = HtmlUtils.htmlEscape(value);
                if (!StringUtils.equalsIgnoreCase(v, value)) {
                    String text = String.format("用户[%s]调用URI[%s]时,传递的参数中可能包含非法值,参数名[%s]:参数值[%s].",
                            HttpUtils.getRemoteIpAddr(req), requestURI, name, value);
                    logger.catching(new IllegalArgumentException(text));
                }
            }
        }
        return true;
    }

}
