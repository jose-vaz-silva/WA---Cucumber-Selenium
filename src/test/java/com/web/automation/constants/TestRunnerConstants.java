package com.web.automation.constants;

import java.io.File;

public class TestRunnerConstants {
	private TestRunnerConstants() {
	}

	public static final String REGEX = "\\D*\\.?\\D*";
	public static final String DOT = ".";
	public static final String UNDERSCORE = "_";
	public static final String ERROR_LIST = "============= ERROR LIST =============";
	public static final String TAB_NAME = "TestCases";
	public static final String YES = "Yes";
	public static final String INSTANTIATING_CLASSES = "Instantiating classes";
	public static final String PLATFORM = "WEB-CHROME";
	public static final String INPUT_TYPE = null; // "EXCEL";
	public static final String AUTOMATION_TOOL = "SELENIUM";
	public static final String SAVE_CONSOLE_AS_TXT = "false";
	public static final String EVIDENCE_ARGS = "255 0 0 7"; // [R] [G] [B] [STROKEWIDTH]
	public static final String DISABLE_ELEMENT_HIGHLIGHTS = "false";
	public static final String APPLICATION_NAME = "Web Automation";
	public static final String FEATURES_DIRECTORY = System.getProperty("user.dir") + File.separator + "src"
			+ File.separator + "test" + File.separator + "resources" + File.separator + "features";
	public static final String CHROME_PROFILE_DIR = System.getProperty("user.dir")
			+ "\\chrome-profiles\\User Data\\selenium-profile";
	public static final String CUCUMBER_CONFIG_FILE_PATH = System.getProperty("user.dir") + File.separator + "src"
			+ File.separator + "test" + File.separator + "java" + File.separator + "com" + File.separator + "web"
			+ File.separator + "automation" + File.separator + "core" + File.separator + "CucumberConfig.java";
	public static final String CUCUMBER_TAGS = System.getProperty("cucumberTags") != null
			? System.getProperty("cucumberTags")
			: "";
	public static final String EXECUTION_SHEET_PATH = System.getProperty("user.dir") + File.separator
			+ "execution-result" + File.separator + "ExecutionSheet.xlsx";
	public static final String EXECUTION_RESULT_DIRECTORY = System.getProperty("user.dir") + File.separator
			+ "execution-result";
	public static final String DATA_PATH = System.getProperty("user.dir") + File.separator + "data";
	public static final String ACCESS_PROPERTIES_PATH = DATA_PATH + File.separator + "access_properties";
	public static final String CONSULTATION_PROPERTIES_PATH = System.getProperty("user.dir") + File.separator + "data"
			+ File.separator + "community_consultation" + File.separator + "consultation.properties";
}