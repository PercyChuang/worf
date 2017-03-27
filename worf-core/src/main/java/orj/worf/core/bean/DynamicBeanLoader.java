package orj.worf.core.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

public class DynamicBeanLoader implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(DynamicBeanLoader.class);
    private ConfigurableApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = ((ConfigurableApplicationContext) applicationContext);
    }

    public void loadBean(DynamicBean dynamicBean) {
        String beanName = dynamicBean.getName();
        BeanDefinitionRegistry beanDefinitonRegistry = (BeanDefinitionRegistry) this.applicationContext
                .getBeanFactory();
        try {
            beanDefinitonRegistry.registerBeanDefinition(beanName, dynamicBean.getBeanDefinition());
            this.applicationContext.getBean(beanName);
        } catch (BeanDefinitionStoreException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
