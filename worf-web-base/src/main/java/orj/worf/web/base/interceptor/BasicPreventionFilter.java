package orj.worf.web.base.interceptor;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class BasicPreventionFilter extends OncePerRequestFilter {

    @Override
    public void initFilterBean() throws ServletException {
        FilterStatus.basicPreventionLoaded = true;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain filterChain) throws ServletException, IOException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("Only Http request supported.");
        }
        EscapedRequestWrapper wRequest = new EscapedRequestWrapper(request);
        filterChain.doFilter(wRequest, response);
    }

}
