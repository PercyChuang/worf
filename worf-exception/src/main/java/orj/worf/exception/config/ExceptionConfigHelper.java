package orj.worf.exception.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import orj.worf.exception.BaseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ExceptionConfigHelper implements ApplicationContextAware, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionConfigHelper.class);
    private static List<ExceptionConfig> exceptionConfigs;
    private static Map<String, Class<? extends BaseException>> exeptionClazzMapping;
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        findAllExceptionConfig();
        assembleExeptionClazzMapping();
    }

    private void findAllExceptionConfig() {
        Map<String, ExceptionConfig> configs = BeanFactoryUtils.beansOfTypeIncludingAncestors(this.applicationContext,
                ExceptionConfig.class, true, false);
        exceptionConfigs = new ArrayList<ExceptionConfig>(configs.size());
        for (ExceptionConfig config : configs.values()) {
            if (logger.isDebugEnabled()) {
                logger.debug("ExceptionConfigHelper found a ExceptionConfig bean:beanName=[{}]", config);
            }
            exceptionConfigs.add(config);
        }
    }

    @SuppressWarnings("unchecked")
    private void assembleExeptionClazzMapping() throws ClassNotFoundException {
        exeptionClazzMapping = new HashMap<String, Class<? extends BaseException>>();
        for (ExceptionConfig config : exceptionConfigs) {
            Map<String, String> exceptionClazzMapping = config.getExceptionClazzMapping();
            if (exceptionClazzMapping != null) {
                for (String codePrefix : exceptionClazzMapping.keySet()) {
                    String className = exceptionClazzMapping.get(codePrefix);
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                                "ExceptionConfigHelper found a exceptionClassNameMapping:codePrefix=[{}] className=[{}]",
                                codePrefix, className);
                    }
                    Class<?> exceptionClazz = Class.forName(className);
                    exeptionClazzMapping.put(codePrefix, (Class<? extends BaseException>) exceptionClazz);
                }
            }
        }
    }

    public static Map<String, Class<? extends BaseException>> getExeptionClazzMapping() {
        if (exeptionClazzMapping == null) {
            throw new RuntimeException(
                    "uninitialized object[orj.worf.exception.config.ExceptionConfigHelper.exeptionClazzMapping]");
        }
        return exeptionClazzMapping;
    }
}
