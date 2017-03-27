package orj.worf.web.base.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringContextHolder implements ServletContextListener {

	private static transient WebApplicationContext springContext;

	public static ApplicationContext getApplicationContext() {
		return springContext;
	}

	@Override
	public void contextDestroyed(final ServletContextEvent sce) {
		springContext = null;
	}

	@Override
	public void contextInitialized(final ServletContextEvent event) {
		springContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
	}

}
