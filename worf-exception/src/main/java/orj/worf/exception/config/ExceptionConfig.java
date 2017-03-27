package orj.worf.exception.config;

import java.util.Map;

public class ExceptionConfig {
    private Map<String, String> exceptionClazzMapping;

    public Map<String, String> getExceptionClazzMapping() {
        return this.exceptionClazzMapping;
    }

    public void setExceptionClazzMapping(final Map<String, String> exceptionClazzMapping) {
        this.exceptionClazzMapping = exceptionClazzMapping;
    }
}