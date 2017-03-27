package orj.worf.core.util;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import orj.worf.core.annotation.ClasspathAnnotationDefinitionScanner;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class AnnotationUtils extends org.springframework.core.annotation.AnnotationUtils {

    @SuppressWarnings("unchecked")
    public static Set<BeanDefinition> findAnnotationBeanDefinition(final String[] basePackages,
            final String annotationType) throws ClassNotFoundException {
        ClasspathAnnotationDefinitionScanner scanner = new ClasspathAnnotationDefinitionScanner();
        scanner.addIncludeFilter(new AnnotationTypeFilter((Class<? extends Annotation>) Class.forName(annotationType)));
        Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
        for (String basePackage : basePackages) {
            for (BeanDefinition bd : scanner.findCandidateComponents(basePackage)) {
                candidates.add(bd);
            }
        }
        return candidates;
    }
}