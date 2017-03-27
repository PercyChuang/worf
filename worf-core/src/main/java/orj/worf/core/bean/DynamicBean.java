package orj.worf.core.bean;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.util.Assert;

public abstract class DynamicBean {
    private final String name;

    public DynamicBean(String name) {
        Assert.notNull(name);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract BeanDefinition getBeanDefinition();
}
