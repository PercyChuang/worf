package orj.worf.web.base.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import orj.worf.exception.util.ExceptionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

public class SimpleMappingExceptionResolver extends org.springframework.web.servlet.handler.SimpleMappingExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(SimpleMappingExceptionResolver.class);

    @Override
    protected ModelAndView doResolveException(final HttpServletRequest request, final HttpServletResponse response,
            final Object handler, final Exception e) {
        if (handler instanceof HandlerMethod && !ExceptionUtils.isHandledException(e, logger)) {
            logger.error("Catch a exception[{}] when invoke controller [{}]", e.getClass().getName(), handler, e);
        }
        String accept = request.getHeader("accept");
        String xReq = request.getHeader("X-Requested-With");
        if (accept != null && accept.indexOf("application/json") > -1 || xReq != null
                && xReq.indexOf("XMLHttpRequest") > -1) {
            response.setStatus(500);
            String exInfo = buildExceptionInfo(e);
            try {
                PrintWriter writer = response.getWriter();
                writer.print(exInfo);
                writer.flush();
            } catch (IOException ex) {
                response.addHeader("error_msg", exInfo);
            }
            return new ModelAndView();
        }
        return super.doResolveException(request, response, handler, e);
    }

    private String buildExceptionInfo(final Exception ex) {
        StringBuilder sb = new StringBuilder(64);
        sb.append("{msg:'");
        sb.append(ExceptionUtils.getRootCauseMessage(ex));
        sb.append("'}");
        return sb.toString();
    }

}