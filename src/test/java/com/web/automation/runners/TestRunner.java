package com.web.automation.runners;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.web.automation.core.Hooks;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
				  glue = { "com.web.automation.steps", "com.web.automation.core" }, 
				  monochrome = true, 
				  strict = true, 
				  plugin = { "html:target/cucumber/cucumber", "json:target/cucumber/cucumber.json" }, 
				  stepNotifications = true, 
			      features = "src/test/resources/features", 
				  tags = {"@CT_AUT_001" }
               )
public class TestRunner {

	@BeforeClass
	public static void setup() {
		Hooks.setExcutionResultTime(new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(new Date()));
	}
}
