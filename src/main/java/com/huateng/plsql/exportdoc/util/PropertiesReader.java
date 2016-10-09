/**
 * 
 */
package com.huateng.plsql.exportdoc.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 配置文件类，使用commons
 * @author pansen
 * @see org.apache.commons.configuration.PropertiesConfiguration
 */
public class PropertiesReader {
	private static final Log LOGGER = LogFactory.getLog(PropertiesReader.class);
	private static PropertiesConfiguration prop;
	
	private PropertiesReader(){}
	
	static{
		try {
			prop = new PropertiesConfiguration("config.properties");
		} catch (ConfigurationException e) {
			LOGGER.error("读取config.properties失败",e);
		}
	}
	
	public static PropertiesConfiguration getReader(){
		return prop;
	}
}
