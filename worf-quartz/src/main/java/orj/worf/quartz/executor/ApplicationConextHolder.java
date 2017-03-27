package orj.worf.quartz.executor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationConextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static ApplicationContext get() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        ApplicationConextHolder.applicationContext = applicationContext;
    }

    public void destroy() {
        setApplicationContext(null);
    }

}
