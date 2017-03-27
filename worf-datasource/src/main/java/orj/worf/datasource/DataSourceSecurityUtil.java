package orj.worf.datasource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import orj.worf.datasource.RJSCipher;

public class DataSourceSecurityUtil {
	private static final Logger logger = LoggerFactory.getLogger(DataSourceSecurityUtil.class);
	private static  Properties props = new Properties();
	
	static {
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("conf/jdbc.properties"));
		} catch (IOException e) {
			logger.error("加载数据源配置文件出错啦..",e);
		}
	}
	
	public static void print() throws UnsupportedEncodingException {
		Set<String> stringPropertyNames = props.stringPropertyNames();
		List<String> sortList = new ArrayList<String>();
		for (String key : stringPropertyNames) {
			String value = props.getProperty(key);
			String encryptData = RJSCipher.encryptData(value);
			value = key+"="+encryptData;
			sortList.add(value);
		}
		Collections.sort(sortList);
		for (String string : sortList) {
			System.out.println(string);
		}
	}
}
