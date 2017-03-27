package orj.worf.core.context;

import java.util.Map;

import orj.worf.core.annotation.AnnotationHandler;
import orj.worf.core.annotation.ClasspathAnnotationDefinitionScanner;
import orj.worf.core.config.ScannerConfigurer;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {

    private ClasspathAnnotationDefinitionScanner scanner;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        try {
            scan(applicationContext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void scan(final ApplicationContext applicationContext) throws Exception {
        Map<String, ScannerConfigurer> basePackagesMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                applicationContext, ScannerConfigurer.class, true, false);
        String[] allBasePackages = null;
        String[] allAnnotationTypes = null;
        for (ScannerConfigurer scannerConfigurer : basePackagesMap.values()) {
            String[] basePackages = scannerConfigurer.getBasePackages();
            if (basePackages != null) {
                allBasePackages = ArrayUtils.addAll(allBasePackages, basePackages);
            }
            String[] annotationTypes = scannerConfigurer.getAnnotationTypes();
            if (annotationTypes != null) {
                allAnnotationTypes = ArrayUtils.addAll(allAnnotationTypes, annotationTypes);
            }
        }
        if (allBasePackages != null && allAnnotationTypes != null) {
            try {
                this.scanner.findAllAnnotationConfig(allAnnotationTypes, allBasePackages);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Map<String, AnnotationHandler> annotationHandlersMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                    applicationContext, AnnotationHandler.class, true, false);
            for (AnnotationHandler handler : annotationHandlersMap.values()) {
                if (handler != null) {
                    handler.handle(this.scanner.getAnnotationConfigs());
                }
            }
        }
    }

    public void setScanner(final ClasspathAnnotationDefinitionScanner scanner) {
        this.scanner = scanner;
    }
}
