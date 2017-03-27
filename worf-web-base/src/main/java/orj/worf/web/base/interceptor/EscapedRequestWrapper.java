package orj.worf.web.base.interceptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.web.util.HtmlUtils;

public class EscapedRequestWrapper extends HttpServletRequestWrapper {

    private final HttpServletRequest request;

    public EscapedRequestWrapper(final HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    public HttpServletRequest getOriginalRequest() {
        return this.request;
    }

    @Override
    public String getHeader(final String name) {
        String value = super.getHeader(name);
        return escape(value);
    }

    @Override
    public String getParameter(final String name) {
        String parameter = super.getParameter(name);
        return escape(parameter);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = super.getParameterMap();
        if (map == null || map.isEmpty()) {
            return map;
        }
        Map<String, String[]> theMap = new HashMap<String, String[]>(map.size());
        for (String key : map.keySet()) {
            theMap.put(key, escapeArray(map.get(key)));
        }
        return Collections.unmodifiableMap(theMap);
    }

    @Override
    public String[] getParameterValues(final String name) {
        String[] values = super.getParameterValues(name);
        if (values == null || values.length == 0) {
            return values;
        }
        return escapeArray(values);
    }

    private String[] escapeArray(final String[] values) {
        int length = values.length;
        String[] theValues = new String[length];
        for (int i = 0; i < length; i++) {
            theValues[i] = escape(values[i]);
        }
        return theValues;
    }

    private String escape(final String value) {
        return HtmlUtils.htmlEscape(value);
    }

}
