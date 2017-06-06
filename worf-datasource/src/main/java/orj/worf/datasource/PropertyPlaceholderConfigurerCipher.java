package orj.worf.datasource;


import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyPlaceholderConfigurerCipher  extends PropertyPlaceholderConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(PropertyPlaceholderConfigurerCipher.class);
	
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		try{
			Enumeration<Object> keys = props.keys();
			while (keys.hasMoreElements()) {
				String key = (String)keys.nextElement();
				key = key.trim();
				logger.info("get prop key:{}",key);
				if(key.startsWith("pwd_")) {
					String pwdValue = props.getProperty(key);
					if (pwdValue != null) {
						pwdValue = pwdValue.trim();
						if (!"".equals(pwdValue)) {
							String decryptData = RJSCipher.decryptData(pwdValue);
							logger.info("the key is startwith pwd_. get the decrypt data :{}",decryptData);
							key = key.substring(4);
							props.setProperty(key, decryptData);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("配置文件设置异常",e);
			throw new FatalBeanException("配置文件配置异常!");
		}
		super.processProperties(beanFactoryToProcess, props);
	}
}
