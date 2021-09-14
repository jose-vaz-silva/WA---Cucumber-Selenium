package com.web.automation.util;

public class SystemUtil {

	public static void setProperty(String key, Object value) {
		System.setProperty(key, value.toString());
	}
}
