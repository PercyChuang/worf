package orj.worf.datasource;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.StringUtils;

public class PropertyPlaceholderConfigurerCipher  extends PropertyPlaceholderConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(PropertyPlaceholderConfigurerCipher.class);
	private static List<String> keys = new ArrayList<String>(Arrays.asList(
			"jdbc.driverclass",
			"jdbc1.driverclass", 
			"jdbc2.driverclass",
			"jdbc3.driverclass", 
			"jdbc4.driverclass",
			
			"jdbc.url",
			"jdbc1.url",
			"jdbc2.url",
			"jdbc3.url",
			"jdbc4.url",
			
			"jdbc.username",
			"jdbc1.username",
			"jdbc2.username",
			"jdbc3.username",
			"jdbc4.username",
			
			"jdbc.password",
			"jdbc1.password",
			"jdbc2.password",
			"jdbc3.password",
			"jdbc4.password",
			
			"jdbc.pool.size.max",
			"jdbc1.pool.size.max",
			"jdbc2.pool.size.max",
			"jdbc3.pool.size.max",
			"jdbc4.pool.size.max"
			));
	
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		try{
			for (String key : keys) {
				String value = props.getProperty(key);
				if (null == value || value.length()==0) {
					value = props.getProperty(getDefaultKey(key));
					props.setProperty(key, value);
				}else{
					String decryptData = RJSCipher.decryptData(value);
					props.setProperty(key, decryptData);
				}
			}
		} catch (Exception e) {
			logger.error("无法解密数据库文件。数据库文件必须要加密存放!",e);
			throw new FatalBeanException("无法解密数据库文件。数据库文件必须要加密存放!");
		}
		super.processProperties(beanFactoryToProcess, props);
	}
	
	public String getDefaultKey(String nullValueKey) {
		 if (nullValueKey.endsWith(".driverclass")){
			 logger.warn(nullValueKey+"值为null，采用默认jdbc.driverclass配置!");
			 return "jdbc.driverclass";
		 }
		 if (nullValueKey.endsWith(".url")){
			 logger.warn(nullValueKey+"值为null，采用默认jdbc.url配置!");
			 return "jdbc.url";
		 }
		 if (nullValueKey.endsWith(".username")){
			 logger.warn(nullValueKey+"值为null，采用默认jdbc.username配置!");
			 return "jdbc.username";
		 }
		 if (nullValueKey.endsWith(".password")){
			 logger.warn(nullValueKey+"值为null，采用默认jdbc.password配置!");
			 return "jdbc.password";
		 }
		 if (nullValueKey.endsWith(".pool.size.max")){
			 logger.warn(nullValueKey+"值为null，采用默认jdbc.pool.size.max配置!");
			 return "jdbc.pool.size.max";
		 }
		 return  null;
	}
}
