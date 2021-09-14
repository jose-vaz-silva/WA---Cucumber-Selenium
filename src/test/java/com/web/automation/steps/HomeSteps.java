package com.web.automation.steps;

import com.web.automation.core.SeleniumCommands;

import io.cucumber.java.pt.Dado;

public class HomeSteps extends SeleniumCommands {

	@Dado("que o sistema de e-commerce é acessado")
	public void acessar_sistema() throws Exception {
		navigate("http://automationpractice.com/index.php");
	}

}
