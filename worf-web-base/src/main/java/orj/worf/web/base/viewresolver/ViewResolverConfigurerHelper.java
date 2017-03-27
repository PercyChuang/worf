package orj.worf.web.base.viewresolver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;
import org.springframework.web.servlet.ViewResolver;

public class ViewResolverConfigurerHelper implements FactoryBean<List<ViewResolver>>, ApplicationContextAware {

    private static final String defaultViewResolversId = "providedByMvcBase";
    private ApplicationContext applicationContext;

    @Override
    public List<ViewResolver> getObject() throws Exception {
        Map<String, ViewResolverConfigurer> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                applicationContext, ViewResolverConfigurer.class, true, false);
        ViewResolverConfigurer defaultViewResolvers = matchingBeans.remove(defaultViewResolversId);
        List<ViewResolverConfigurer> customizeViewResolvers = new ArrayList<ViewResolverConfigurer>(
                matchingBeans.values());
        OrderComparator.sort(customizeViewResolvers);
        List<ViewResolver> viewResolvers = new LinkedList<ViewResolver>();
        for (ViewResolverConfigurer vc : customizeViewResolvers) {
            viewResolvers.addAll(vc.getViewResolvers());
        }
        viewResolvers.addAll(defaultViewResolvers.getViewResolvers());
        return viewResolvers;
    }

    @Override
    public Class<?> getObjectType() {
        return List.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
