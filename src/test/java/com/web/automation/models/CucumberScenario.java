package com.web.automation.models;

import com.web.automation.util.StringManager;

public class CucumberScenario {
	private static String FeatureName;
	private static String ScenarioName;
	private static String FinalResult;

	private CucumberScenario() {
	};

	public static String getFeatureName() {
		return FeatureName;
	}

	public static void setFeatureName(String featureName) {
		FeatureName = featureName;
	}

	public static String getScenarioName() {
		return ScenarioName;
	}

	public static void setScenarioName(String scenarioName) {
		ScenarioName = scenarioName;
	}

	public static String getFinalResult() {
		return FinalResult;
	}

	public static void setFinalResult(String finalResult) {
		FinalResult = finalResult;
	}

	public static String getExampleNumber() {
		return StringManager.substringByRegex(getScenarioName(), "(\\s*\\-\\s*EX\\d+\\s*)$").replaceAll("\\s*\\-\\s*",
				"");
	}
}
