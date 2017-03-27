package orj.worf.web.base.scanner;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import orj.worf.core.annotation.AnnotationHandler;
import orj.worf.web.base.tag.UrlConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;

public class ClassPathUrlConstantAnnotationHandler implements AnnotationHandler {

    private static final Logger logger = LoggerFactory.getLogger(ClassPathUrlConstantAnnotationHandler.class);
    private static Map<String, String> constants = new ConcurrentHashMap<String, String>(50);

    @Override
    public void handle(final Map<String, Set<BeanDefinition>> annotationConfigs) {
        Set<BeanDefinition> allConstantAnnotationConfigs = annotationConfigs.get(UrlConstant.class.getName());
        for (BeanDefinition candidate : allConstantAnnotationConfigs) {
            try {
                findUrlConstant(candidate);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void findUrlConstant(final BeanDefinition candidate) throws Exception {
        String beanClassName = candidate.getBeanClassName();
        Class<?> clazz = Class.forName(beanClassName);
        String namespace = clazz.getAnnotation(UrlConstant.class).namespace();
        for (Field field : clazz.getFields()) {
            String fieldName = field.getName();
            String fieldValue = (String) field.get(null);
            constants.put(namespace + "." + fieldName, fieldValue);
            if (logger.isDebugEnabled()) {
                logger.debug("finded url constant [{}={}] in {}", new Object[] { fieldName, fieldValue, beanClassName });
            }
        }
    }

    public static Map<String, String> getConstants() {
        return constants;
    }

}
