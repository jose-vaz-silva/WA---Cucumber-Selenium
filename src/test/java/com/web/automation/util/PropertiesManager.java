package com.web.automation.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {
	
	private  String propertiesFilePath;
	
	public PropertiesManager (String propertiesFilePath){
		this.propertiesFilePath = propertiesFilePath;
	}
	
	public Properties getProperties() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(propertiesFilePath);
		props.load(file);
		return props;

	}

	public void setProperty(String key, String value) throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(
				propertiesFilePath);
		props.load(file);
		file.close();
		props.put(key, value);
		props.store(new FileOutputStream(propertiesFilePath), null);

	}
	
	public void removeProp(String key) throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(
				propertiesFilePath);
		props.load(file);
		file.close();
		props.remove(key);
		props.store(new FileOutputStream(propertiesFilePath), null);

	}

}
