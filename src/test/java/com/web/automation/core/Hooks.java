package com.web.automation.core;

import static com.web.automation.constants.TestRunnerConstants.CHROME_PROFILE_DIR;
import static com.web.automation.util.FilesManager.deleteFile;
import static com.web.automation.util.FilesManager.getFileList;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.WebDriver;

import com.web.automation.models.CucumberScenario;
import com.web.automation.util.GenerateEvidences;
import com.web.automation.util.StringManager;

import cucumber.api.Result;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

	public static String evidencesPath = null;
	public static String timeStampStart = null;
	private long initStopwatch, elapsedTime;
	private static String excutionResultTime;
	public static WebDriver webDriver;

	@After
	public void fechar(Scenario scenario) throws Exception {
		DriverFactory.killDriver();
		setElapsedTime(System.currentTimeMillis() - initStopwatch);
	}

	@Before
	public void before(Scenario scenario) throws Exception {

		webDriver = DriverFactory.getDriver();
		String dir = System.getProperty("user.dir");
		timeStampStart = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(new Date());
		setFeatureFileNameFromScenarioId(scenario);
		setScenarioName(scenario);
		logScenarioInformations();
		evidencesPath = dir + File.separator + "output" + File.separator + timeStampStart + File.separator
				+ CucumberScenario.getFeatureName() + File.separator + CucumberScenario.getScenarioName();
		GenerateEvidences.evidence_count = 0;
	}

	private void setFeatureFileNameFromScenarioId(Scenario scenario) throws Exception {
		String rawFeatureName = scenario.getId().split(";")[0].replaceAll("\\-+", " ");
		List<String> featurePathSplitedList = Arrays.asList(rawFeatureName.split("(\\/|\\\\)"));
		int lastIndex = featurePathSplitedList.size() - 1;
		String featureName = StringManager.substringByRegex(featurePathSplitedList.get(lastIndex), "(.*)(?=\\.feature)")
				.replaceAll("\\s+$", "");
		if (featureName == null || featureName == "") {
			throw new Exception("The name of featrue is not filled");
		}
		CucumberScenario.setFeatureName(featureName.replaceAll("%20", " "));
	}

	private void setScenarioName(Scenario scenario) {
		CucumberScenario.setScenarioName(scenario.getName().replaceAll("\"", "").replaceAll("\\.", ""));
	}

	private static void setScenarioFinalResult(Scenario scenario) {
		String finalResult = null;
		try {
			Class<?> clasz = ClassUtils.getClass("cucumber.runtime.java.JavaHookDefinition$ScenarioAdaptor");
			Field field = FieldUtils.getField(clasz, "scenario", true);
			field.setAccessible(true);
			Object objectScenario = field.get(scenario);
			Field fieldStepResults = objectScenario.getClass().getDeclaredField("stepResults");
			fieldStepResults.setAccessible(true);
			@SuppressWarnings("unchecked")
			ArrayList<Result> results = (ArrayList<Result>) fieldStepResults.get(objectScenario);
			for (Result result : results) {
				if (result.getError() != null) {
					String[] resultErrorMessage = result.getErrorMessage().split("Build info:");
					finalResult = resultErrorMessage[0].contains("NullPointerException") ? "NullPointerException"
							: resultErrorMessage[0].replaceFirst("([A-Z,a-z,0-9]+\\.?)+\\:\\s", "");
					break;
				} else {
					finalResult = "Done";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		CucumberScenario.setFinalResult(finalResult);
	}

	/**
	 * Delete temporary chrome profile files
	 * 
	 * @throws Exception
	 */
	public static void deleteTemporaryChromeProfileFiles() throws Exception {
		File[] files = getFileList(CHROME_PROFILE_DIR);
		File[] filesDefaultProfile = getFileList(CHROME_PROFILE_DIR + File.separator + "Default");
		ArrayList<String> fileListNotDelete = new ArrayList<String>(
				Arrays.asList("Default", "Local State", "Cookies", "Extensions"));
		Arrays.asList(files).stream().filter(file -> !fileListNotDelete.contains(file.getName()))
				.forEach(file -> deleteFile(file));
		Arrays.asList(filesDefaultProfile).stream().filter(file -> !fileListNotDelete.contains(file.getName()))
				.forEach(file -> deleteFile(file));
	}

	public void logScenarioInformations() {
		System.out.println("Feature: " + CucumberScenario.getFeatureName());
		System.out.println("Scenario: " + CucumberScenario.getScenarioName() + "\n");
	}

	public static String getExcutionResultTime() {
		return excutionResultTime;
	}

	public static void setExcutionResultTime(String excutionResultTime) {
		Hooks.excutionResultTime = excutionResultTime;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

}