package com.web.automation.core;

import static com.web.automation.constants.SeleniumTestConstants.CIRCUNFLEJO;
import static com.web.automation.constants.SeleniumTestConstants.DOLAR;
import static com.web.automation.constants.SeleniumTestConstants.REGEX;
import static com.web.automation.constants.TimeOutConstants.MAX_SECONDS;
import static com.web.automation.core.Hooks.webDriver;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.web.automation.util.FrameManager;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.E;
import cucumber.api.java.pt.Entao;
import cucumber.api.java.pt.Então;
import cucumber.api.java.pt.Quando;

public class SeleniumCommands {
	WebDriverWait wait;
	@SuppressWarnings("unused")
	private XSSFWorkbook workbook;
	private static WebDriver driver;

	public SeleniumCommands() {
		driver = webDriver;
	}

	/**
	 * Browse to a page
	 *
	 * @param url URL of the application
	 * @throws Exception
	 */
	public void navigate(String url) throws Exception {
		// TODO Auto-generated catch block
		if (!url.isEmpty()) {
			DriverFactory.getDriver().navigate().to(url);
		} else {
			throw new Exception("'Url' está vazia");
		}
	}

	public void click(WebElement element) {
		element.click();
	}

	public void quitBrowser() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

	public void closeBrowser() {
		driver.close();
	}

	public void sendKeys(WebElement element, String value) {
		element.sendKeys(value);
	}

	public void sendKey(WebElement element, Keys key) {
		element.sendKeys(key);
	}

	public static WebElement getWebElement(By by) {
		WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
		return element;
	}

	public static List<WebElement> getWebElements(By locator) {
		return driver.findElements(locator);
	}

	public static boolean waitElementWithException(By locator, int timeOut) throws Exception {
		boolean elementFinded = false;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			elementFinded = true;
			return elementFinded;
		} catch (NoSuchElementException ex) {
			throw new NoSuchElementException("No elements found with " + locator + " locator");
		} catch (TimeoutException ex) {
			throw new TimeoutException("Time (" + timeOut + ") exceeded to find element: " + locator);
		}
	}

	public static boolean waitElement(By locator, int timeOut) throws Exception {
		boolean status = false;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			WebElement webElement = wait.withTimeout(Duration.ofSeconds(timeOut)).pollingEvery(Duration.ofMillis(500))
					.ignoring(NoSuchElementException.class)
					.until(ExpectedConditions.visibilityOf(getWebElement(locator)));
			if (webElement.isDisplayed()) {
				status = true;
			}
		} catch (Exception e) {
			if (e.getMessage().contains("Time (" + Integer.toString(timeOut) + ") exceeded to find element:")
					|| e.getMessage().contains("Expected condition failed: waiting for presence of element located by:")
					|| e.getMessage()
							.contains("Expected condition failed: waiting for visibility of element located by")) {
				status = false;
			}
		}
		return status;
	}

	// public static void waitElement(By locator, int timeOut) throws Exception {
	// try {
	// WebDriverWait wait = new WebDriverWait(driver, timeOut);
	// wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	// } catch (NoSuchElementException ex) {
	// throw new NoSuchElementException("No elements found with " + locator + "
	// locator");
	// } catch (TimeoutException ex) {
	// throw new TimeoutException("Time (" + timeOut + ") exceeded to find element:
	// " + locator);
	// }
	public void waitElementToBeClicable(By locator, int timeOut) throws NoSuchElementException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
			wait.until(ExpectedConditions.elementToBeClickable(locator));
		} catch (NoSuchElementException ex) {
			throw new NoSuchElementException("No elements found with " + locator + " locator");
		} catch (TimeoutException ex) {
			throw new TimeoutException("Time (" + MAX_SECONDS + ") exceeded to find element: " + locator);
		}
	}

	public static boolean waitPresenceOfElement(By element, Integer waitTime) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.presenceOfElementLocated(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean waitPresenceOfElementAlternative(By locator, Integer waitTime) throws Exception {
		boolean finded = false;
		int startTime = 0;
		while (finded == false && startTime < waitTime) {
			finded = isPresent(locator);
			TimeUnit.MILLISECONDS.sleep(500);
			startTime++;
		}

		return finded;
	}

	public static void waitPresenceOfElement(By element, Integer waitTime, String exceptionMessage) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.presenceOfElementLocated(element));
			scrollToElementInTheMiddle(element);
		} catch (Exception e) {
			throw new Exception(exceptionMessage + "\n" + e.getMessage());
		}
	}

	public static boolean waitVisibilityOfElement(By element, Integer waitTime) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.visibilityOfElementLocated(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean waitVisibilityOfElementAlternative(By locator, Integer waitTime) throws Exception {
		boolean isVisible = false;
		int startTime = 0;
		while (isVisible == false && startTime < waitTime) {
			isVisible = isDisplayed(locator);
			if (!isVisible)
				TimeUnit.MILLISECONDS.sleep(500);
			startTime++;
		}

		return isVisible;
	}

	public static boolean waitVisibilityAndPresenceOfElement(By element, Integer waitTime) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.presenceOfElementLocated(element));
			scrollToElementInTheMiddle(element);
			wait.until(ExpectedConditions.visibilityOfElementLocated(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void waitVisibilityAndPresenceOfElement(By element, Integer waitTime, String exceptionMessage)
			throws Exception {
		try {
			if (waitPresenceOfElementAlternative(element, waitTime)) {
				if (!waitVisibilityOfElementAlternative(element, waitTime)) {
					throw new Exception("The element '" + element + "' didn’t become visible on the page in '"
							+ waitTime + "' seconds");
				}
				scrollToElementInTheMiddle(element);
			} else {
				throw new Exception("The element '" + element + "' didn’t become present on the page in '" + waitTime
						+ "' seconds");
			}

		} catch (Exception e) {
			throw new Exception(exceptionMessage + "\r\n" + e);
		}
	}

	public static boolean waitCheckPresenceOfElement(WebElement element, Integer waitTime) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			wait.until(ExpectedConditions.visibilityOf(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static Boolean waitPresenceOfFrame(By element, Integer waitTime) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(element));
		return isPresent(element);
	}

	public static void waitPresenceOfElement(WebElement element, Integer waitTime) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public static void waitLoadPageComplete() {
		WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
		wait.until((ExpectedCondition<Boolean>) d -> ((JavascriptExecutor) d)
				.executeScript("return document.readyState").equals("complete"));
	}

	public void clickJs(By by) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].click();", getWebElement(by));
	}

	public void scrollDynamic(By by) {
		WebElement element = driver.findElement(by);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void clickActions(By by) {
		WebElement element = getWebElement(by);
		Actions actions = new Actions(driver);
		actions.moveToElement(element).click().build().perform();
	}

	public String getTextElement(By by) {
		WebElement element = getWebElement(by);
		return element.getText();
	}

	public String getOptionSelected(By by) {
		Select select = new Select(getWebElement(by));
		WebElement option = select.getFirstSelectedOption();
		return option.getText();
	}

	public static void scrollToElement(By by) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getWebElement(by));
	}

	public static void scrollToElementPosition(By by) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			try {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollBy(0, arguments[1]);",
						getWebElement(by), getWebElement(by).getLocation().y);
				break;
			} catch (Exception e) {
				if (i == 9)
					throw e;
				else
					TimeUnit.MILLISECONDS.sleep(500);
			}
		}
	}

	public static void scrollToElementIfNeeded(By element) throws InterruptedException {
		JavascriptExecutor je = ((JavascriptExecutor) driver);
		je.executeScript("arguments[0].scrollIntoViewIfNeeded(true);", getWebElement(element));
		Thread.sleep(500);
	}

	public static void scrollToElement(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public static void scrollToElementActions(By locator) {
		Actions action = new Actions(driver);
		action.moveToElement(getWebElement(locator)).build().perform();
	}

	/**
	 * Scroll down page by pixel using java script
	 *
	 * @param pixel
	 */
	public void scrollPageByPixel(String pixel) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0," + pixel + ")");
	}

	/**
	 * Scroll to element while is not visible
	 *
	 * @param element
	 */
	public void scrollToElementNotVisible(By element) throws Exception {
		int waitCont = 0;
		do {
			scrollToElementAlternative(element);
			waitCont++;
			if (waitCont == 5)
				throw new NoSuchElementException("Elemento nao encontrado na pagina");
		} while (!isPresent(element) || !isDisplayed(element));
	}

	public static String getLocalhost() throws UnknownHostException {
		InetAddress inet = InetAddress.getLocalHost();
		String localhost = inet.toString().split("/")[1];
		return localhost;
	}

	/**
	 * get files in directory with filter
	 *
	 * @param directoryPath
	 * @param filterFile
	 * @return
	 */
	public static File[] getDirectoryFilesWithFilter(String directoryPath, String filterFile) {
		File directory = new File(directoryPath);
		File[] arquivos = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String nome) {
				return nome.matches(filterFile);
			}
		});
		return arquivos;
	}

	public void newTab() {
		((JavascriptExecutor) driver).executeScript("window.open();");
	}

	public void clickActionsJS(By locator) throws Exception {
		Actions actions = new Actions(driver);
		actions.moveToElement(getWebElement(locator)).click().build().perform();
	}

	/**
	 * Browse to a page
	 *
	 * @param url URL of the application
	 */
	void navigateToUrl(String url) throws Exception {
		driver.navigate().to(url);
	}

	/**
	 * Switch to second tab, closes the first
	 */
	public static void switchTab() {
		String oldTab = driver.getWindowHandle();
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		tabs.remove(oldTab);
		driver.close();
		driver.switchTo().window(tabs.get(0));
	}

	/**
	 * SendKeys
	 *
	 * @param element
	 * @param value
	 * @throws ElementNotFoundException
	 */
	public void sendKeysActions(By locator, String value) throws Exception {
		Actions actions = new Actions(driver);
		int i = 0;
		WebElement webEleemnt = getWebElement(locator);
		while (!webEleemnt.getText().equals(value) && i < 5) {
			webEleemnt.clear();
			TimeUnit.MICROSECONDS.sleep(100);
			actions.moveToElement(getWebElement(locator)).sendKeys(getWebElement(locator), value).build().perform();
			i++;
		}
	}

	public void sendKeyActions(By locator, CharSequence keys) throws Exception {
		Actions actions = new Actions(driver);
		WebElement webElement = getWebElement(locator);
		actions.moveToElement(webElement).sendKeys(webElement, keys).build().perform();
	}

	/**
	 * Send keys
	 *
	 * @param finder
	 * @param value
	 * @throws Exception
	 */
	public void sendEnter(By locator) throws Exception {
		if (waitVisibilityAndPresenceOfElement(locator, MAX_SECONDS)) {
			scrollToElement(locator);
			getWebElement(locator).sendKeys(Keys.ENTER);
		} else {
			throw new Exception("Element not found: " + getWebElement(locator).getText());
		}
	}

	public static boolean isPresent(By locator) {
		Boolean isPresent = getWebElements(locator).size() > 0;
		return isPresent;
	}

	public static void isPresent(By locator, String exceptionMessage) throws Exception {
		if (!(getWebElements(locator).size() > 0))
			throw new Exception(exceptionMessage);
	}

	/**
	 * Fill the field with a certain value
	 *
	 * @param finder    Element to action
	 * @param value     Value to be filled
	 * @param sendEnter If true send enter key at the end of filling; If false not
	 *                  send enter key
	 */
	public void sendKeys(By finder, String value, Boolean sendEnter) throws Exception {
		WebElement element = getWebElement(finder);
		if (waitVisibilityAndPresenceOfElement(finder, MAX_SECONDS)) {
			scrollToElement(finder);
			WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
			wait.until(ExpectedConditions.elementToBeClickable(finder));
			for (int c = 0; c < 10; c++) {
				try {
					element.clear();
					element.sendKeys(value);
					break;
				} catch (Exception e) {
					if (c == 9) {
						throw e;
					} else {
						TimeUnit.MILLISECONDS.sleep(500);
					}
				}
			}
			if (sendEnter) {
				element.sendKeys(Keys.ENTER);
			}
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	public void justSendKeys(By finder, String value, Boolean sendEnter) throws Exception {
		WebElement element = getWebElement(finder);
		value = value.replace("/", Keys.DIVIDE);
		element.sendKeys(value);
		if (sendEnter) {
			Thread.sleep(1000);
			sendEnter(finder);
		}
	}

	public void sendKeysValidateLazy(By locator, String value, Boolean sendEnter) throws Exception {
		WebElement finder = getWebElement(locator);
		// Actions actions = new Actions(driver);
		waitElement(locator, MAX_SECONDS);
		if (isPresent(locator)) {
			scrollToElement(locator);
			int i = 0;
			while (!finder.getText().equals(value.replace(Keys.DIVIDE, "/")) && i < 5) {
				value = value.replace("/", Keys.DIVIDE);
				finder.clear();
				for (Character c : value.toCharArray()) {
					finder.sendKeys(c.toString());
					Thread.sleep(200);
				}
				i++;
			}
			if (sendEnter) {
				Thread.sleep(1000);
				finder.sendKeys(Keys.ENTER);
			}
		} else {
			throw new NoSuchElementException("Elemento " + finder.toString() + " não encontado! ");
		}
	}

	public void sendKeysSimple(By finder, String value) throws Exception {
		WebElement element = getWebElement(finder);
		if (waitVisibilityAndPresenceOfElement(finder, MAX_SECONDS)) {
			scrollToElement(finder);
			WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
			wait.until(ExpectedConditions.elementToBeClickable(finder));
			for (int c = 0; c < 10; c++) {
				try {
					element.clear();
					element.sendKeys(value);
					break;
				} catch (Exception e) {
					if (c == 9) {
						throw e;
					} else {
						TimeUnit.MILLISECONDS.sleep(500);
					}
				}
			}
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	/**
	 * get current Url
	 *
	 * @return
	 */
	public static String getCurrentPageUrl() {
		return driver.getCurrentUrl();
	}

	/**
	 * Send keys by script
	 *
	 * @param finder
	 * @param value
	 * @throws ElementNotFoundException
	 * @throws Exception
	 */
	public void sendKeysScript(By finder, String value) throws Exception, Exception {
		if (isPresent(finder) && waitVisibilityAndPresenceOfElement(finder, MAX_SECONDS)) {
			scrollToElement(finder);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].value='" + value + "';", driver.findElement(finder));
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	public void jusstSendKeysScript(By finder, String value) throws Exception, Exception {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].value='" + value + "';", driver.findElement(finder));
	}

	void sendKeysLazy(By finder, String value, Boolean sendEnter) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(finder));
		waitElement(finder, MAX_SECONDS);
		if (isPresent(finder)) {
			scrollToElement(finder);
			value = value.replace("/", Keys.DIVIDE);
			element.click();
			element.clear();
			for (Character c : value.toCharArray()) {
				element.sendKeys(c.toString());
				Thread.sleep(50);
			}
			if (sendEnter) {
				Thread.sleep(1000);
				element.sendKeys(Keys.ENTER);
			}
			Thread.sleep(1000);
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	/**
	 * Fill the field with a certain value slowly
	 *
	 * @param finder Element to action
	 * @param value  Value to be filled
	 */
	void sendKeysPause(By finder, String value) throws Exception {
		WebElement element = getWebElement(finder);
		waitElement(finder, MAX_SECONDS);
		if (isPresent(finder)) {
			scrollToElement(finder);
			value = value.replace("/", Keys.DIVIDE);
			element.click();
			element.clear();
			for (Character c : value.toCharArray()) {
				element.sendKeys(c.toString());
				Thread.sleep(800);
			}
			if (!element.getAttribute("value").equals(value.replace(Keys.DIVIDE, "/"))) {
				Thread.sleep(1000);
				element.click();
				element.clear();
				for (Character c : value.toCharArray()) {
					element.sendKeys(c.toString());
					Thread.sleep(800);
				}
				seleniumWaitForAttribute(finder, "value", value.replace(Keys.DIVIDE, "/"));
			}
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	private void seleniumWaitForAttribute(By finder, String attribute, String expectedValue) throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
			wait.until(ExpectedConditions.attributeToBe(finder, attribute, expectedValue));
		} catch (TimeoutException t) {
			throw new Exception("Expected condition failed: waiting for element found by xpath: " + finder.toString()
					+ " to have value: " + expectedValue + " but the current value is: " + toString());
		}
	}

	public void sendKey(By finder, Keys key) throws Exception {
		WebElement element = getWebElement(finder);
		element.sendKeys(key);
	}

	/**
	 * Click on the object
	 *
	 * @param finder Element to action
	 * @param b
	 */
	public static void click(By finder) throws Exception {
		waitVisibilityAndPresenceOfElement(finder, MAX_SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(finder));
		scrollToElementAlternative(finder);
		int countClicks = 0;
		while (countClicks < 5) {
			try {
				element.click();
				break;
			} catch (Exception e) {
				TimeUnit.SECONDS.sleep(1);
				if (countClicks == 5)
					throw new Exception(e);
			}
			countClicks++;
		}
	}

	public void clickWithoutScrolling(By finder) throws Exception {
		waitElement(finder, MAX_SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(finder));
		if (isDisplayed(finder) && isClickable(finder)) {
			int countClicks = 0;
			while (countClicks < 5) {
				try {
					element.click();
					break;
				} catch (Exception e) {
					TimeUnit.SECONDS.sleep(1);
					if (countClicks == 5)
						throw new Exception(e);
				}
				countClicks++;
			}
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	public void clickSimple(By finder) throws Exception {
		if (isDisplayed(finder) && isClickable(finder)) {
			WebElement element = getWebElement(finder);
			element.click();
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	public void clickAlternative(By element) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 5);
		WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(element));
		if (isDisplayed(element) && isClickable(element)) {
			Actions actions = new Actions(driver);
			actions.moveToElement(webElement).click().build().perform();
		}
	}

	public void doubleClick(By finder) throws Exception {
		waitElement(finder, MAX_SECONDS);
		if (isPresent(finder)) {
			scrollToElement(finder);
			Actions act = new Actions(driver);
			act.moveToElement(driver.findElement(finder)).doubleClick().build().perform();
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	/**
	 * Verify if the value exists on the combo box options, if exists the option is
	 * selected if not exists, nothing is done.
	 *
	 * @param finder comboBox element
	 * @param value  value that will be selected
	 */
	void verifyAndSelectCombo(By finder, String value) throws Exception {
		waitElement(finder, MAX_SECONDS);
		if (!isPresent(finder)) {
			throw new Exception("This element was not found: " + finder.toString());
		} else if (viableValue(finder, value)) {
			Select select = new Select(getWebElement(finder));
			select.selectByVisibleText(value);
		} else {
			return;
		}
	}

	private Boolean viableValue(By comboBox, String value) {
		try {
			List<String> options = Arrays.asList(comboBoxOptions(comboBox, false));
			return options.contains(value);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Return all viable options of a comboBox
	 *
	 * @param comboBox     ComboBox element
	 * @param discardFirst If true, discard the first element of the comboBox
	 */
	private String[] comboBoxOptions(By comboBox, Boolean discardFirst) throws Exception {
		StringBuilder comboBoxOptions = new StringBuilder();
		int checkComboBox = discardFirst ? 1 : 0;
		WebElement mySelectElement = getWebElement(comboBox);
		Select dropdown = new Select(mySelectElement);
		List<WebElement> dropdownElement = dropdown.getOptions();
		for (int j = checkComboBox; j < dropdownElement.size(); j++) {
			comboBoxOptions.append(dropdownElement.get(j).getText()).append('\n');
		}
		return comboBoxOptions.toString().split("\n");
	}

	void selectCombo(By finder, String visibleText) throws Exception {
		waitElement(finder, MAX_SECONDS);
		if (!isPresent(finder)) {
			throw new Exception("This element was not found: " + finder.toString());
		} else if (!viableValue(finder, visibleText)) {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.elementToBeClickable(finder));
			if (!viableValue(finder, visibleText)) {
				throw new Exception("Selected value is not viable: " + visibleText);
			}
		}
		Select select = new Select(getWebElement(finder));
		select.selectByVisibleText(visibleText);
	}

	void selectComboByValue(By finder, String value) throws Exception {
		waitElement(finder, MAX_SECONDS);
		if (!isPresent(finder)) {
			throw new Exception("This element was not found: " + finder.toString());
		}
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(finder));
		scrollToElementInTheMiddle(finder);
		Select select = new Select(getWebElement(finder));
		select.selectByValue(value);
	}

	/**
	 * Select select by index
	 *
	 * @param finder
	 * @param index
	 * @throws Exception
	 */
	void selectCombo(By finder, int index) throws Exception {
		waitElement(finder, MAX_SECONDS);
		if (!isPresent(finder)) {
			throw new Exception("This element was not found: " + finder.toString());
		}
		Select select = new Select(getWebElement(finder));
		select.selectByIndex(index);
	}

	public void sendKeysValidate(By finder, String value, Boolean sendEnter) throws Exception {
		WebElement element = getWebElement(finder);
		waitVisibilityAndPresenceOfElement(finder, MAX_SECONDS);
		if (isPresent(finder)) {
			scrollToElement(finder);
			value = value.replace("/", Keys.DIVIDE);
			click(finder);
			element.clear();
			element.sendKeys(value);
			if (!element.getAttribute("value").equals(value.replace(Keys.DIVIDE, "/"))) {
				Thread.sleep(1000);
				element.clear();
				element.sendKeys(value);
				seleniumWaitForAttribute(finder, "value", value.replace(Keys.DIVIDE, "/"));
			}
			if (sendEnter) {
				Thread.sleep(1000);
				element.sendKeys(Keys.ENTER);
			}
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
	}

	/**
	 * Move to frame and select a value in a comboBox
	 *
	 * @param finder ComboBox element
	 * @param value  Value that will be selected
	 */
	void moveToFrameAndselectCombo(By finder, String value) throws Exception {
		FrameManager.findFrame(finder);
		selectCombo(finder, value);
		FrameManager.moveToRootFrame();
	}

	/**
	 * Switch to last window opened
	 */
	void switchToNewWindow() throws Exception {
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
	}

	public static void moveFrameAndClick(By finder) throws Exception {
		FrameManager.findFrame(finder);
		waitElement(finder, MAX_SECONDS);
		if (isPresent(finder)) {
			getWebElement(finder).click();
		} else {
			throw new Exception("This element was not found: " + finder.toString());
		}
		FrameManager.moveToRootFrame();
	}

	/**
	 * Check if the text of an element is equal to a string
	 *
	 * @param element      element that has the value
	 * @param expectedText text to be compared
	 */
	public void checkText(By element, String expectedText) throws Exception {
		waitElement(element, MAX_SECONDS);
		if (isPresent(element)) {
			scrollToElement(element);
			String actualText = (String) getWebElement(element).getText();
			if (!expectedText.equals(actualText)) {
				throw new Exception(
						"The text of the element is " + actualText + " and the expected text is " + expectedText);
			}
		} else {
			throw new Exception("This element was not found: " + element.toString());
		}
	}

	/**
	 * Move to frame and check if the text of an element is equal to a string
	 *
	 * @param element      element that has the value
	 * @param expectedText text to be compared //
	 */
	void moveToFrameAndCheckText(By element, String expectedText) throws Exception {
		FrameManager.findFrame(element);
		checkText(element, expectedText);
		FrameManager.moveToRootFrame();
	}

	/**
	 * Check if the value of an element is equal to a string
	 *
	 * @param element      element that has the value
	 * @param expectedText text to be compared
	 */
	void checkValue(By element, String expectedText) throws Exception {
		waitElement(element, MAX_SECONDS);
		if (isPresent(element)) {
			scrollToElement(element);
			String actualText = (String) getWebElement(element).getAttribute("value");
			if (!expectedText.equals(actualText)) {
				throw new Exception(
						"The value of the element is " + actualText + " and the expected value is " + expectedText);
			}
		} else {
			throw new Exception("This element was not found: " + element.toString());
		}
	}

	/**
	 * Get a attribute of the element
	 *
	 * @param element   Element Object
	 * @param attribute Attribute Name
	 * @return Value of the attribute of the element.
	 */
	String getAttribute(By element, String attribute) throws Exception {
		waitElement(element, MAX_SECONDS);
		if (isPresent(element)) {
			scrollToElement(element);
			String elementAttribute = (String) getWebElement(element).getAttribute(attribute);
			if (!elementAttribute.isEmpty()) {
				return elementAttribute;
			} else {
				throw new Exception("The attribute " + attribute + " of the element is empty");
			}
		} else {
			throw new Exception("This element was not found: " + element.toString());
		}
	}

	String getAttributeNotWait(By element, String attribute) throws Exception {
		String elementAttribute = driver.findElement(element).getAttribute(attribute);
		if (!elementAttribute.isEmpty()) {
			return elementAttribute;
		} else {
			throw new Exception("The attribute " + attribute + " of the element is empty");
		}
	}

	/**
	 * Get the text of an element
	 *
	 * @param element Element that has the value
	 * @return The text of the element
	 */
	public String getText(By element) throws Exception {
		waitPresenceOfElement(element, MAX_SECONDS);
		if (isPresent(element)) {
			scrollToElementAlternative(element);
			String elementText = (String) getWebElement(element).getText();
			if (!elementText.isEmpty()) {
				return elementText;
			} else {
				throw new Exception("Text of the element is empty");
			}
		} else {
			throw new Exception("This element was not found: " + element.toString());
		}
	}

	/**
	 * Get the value of an element
	 *
	 * @param element Element that has the value
	 * @return The value of the element
	 * @throws EFABasicException        Exception that can be throw if the value of
	 *                                  the element is empty
	 * @throws ElementNotFoundException Exception that can be throw if the element
	 *                                  are not found
	 */
	public String getValue(By element) throws Exception {
		waitElement(element, MAX_SECONDS);
		if (isPresent(element)) {
			scrollToElement(element);
			String elementText = (String) getWebElement(element).getAttribute("value");
			if (!elementText.isEmpty()) {
				return elementText;
			} else {
				throw new Exception("Value of the element is empty");
			}
		} else {
			throw new Exception("This element was not found: " + element.toString());
		}
	}

	public boolean isElementTextEmpty(By finder) throws Exception {
		String fieldValue = new String();
		try {
			fieldValue = getText(finder);
			if (fieldValue.isEmpty() || fieldValue.equals(""))
				return true;
			else
				return false;
		} catch (Exception e) {
			if (e.getMessage().equals("Text of the element is empty"))
				return true;
			else
				throw e;
		}
	}

	/**
	 * Check if an element exists on the page
	 *
	 * @param element     Element to check
	 * @param shouldExist True if the element should exist or false if it shouldn't
	 */
	public void elementExists(By element, Boolean shouldExist) throws Exception {
		waitElement(element, MAX_SECONDS);
		if (shouldExist) {
			if (isPresent(element))
				scrollToElement(element);
			else
				throw new Exception("This element was not found: " + element.toString());
		} else {
			if (isPresent(element))
				throw new Exception("The element exists but it shouldn't");
		}
	}

	/**
	 * Return an attribute from an element using javascript
	 *
	 * @param element   Element that will be used
	 * @param attribute The desired attribute
	 */
	String getAttributeJS(By element, String attribute) throws Exception {
		return (String) ((JavascriptExecutor) driver).executeScript("return arguments[0]." + attribute,
				getWebElement(element));
	}

	/**
	 * Check if a specific attribute exists
	 *
	 * @param element   Element to be checked
	 * @param attribute The desired attribute
	 */
	Boolean attributeExists(By element, String attribute) throws Exception {
		waitElement(element, MAX_SECONDS);
		if (isPresent(element)) {
			scrollToElement(element);
			String elementText = (String) ((JavascriptExecutor) driver)
					.executeScript("return arguments[0]." + attribute, element);
			return elementText != null;
		} else
			throw new Exception("This element was not found: " + element.toString());
	}

	/**
	 * Reload the current page
	 */
	void refresh() throws Exception {
		driver.navigate().refresh();
	}

	/**
	 * Check if an element is clickable
	 *
	 * @param element Element to be checked
	 */
	static boolean isClickable(By element) throws Exception {
		WebElement webElement = getWebElement(element);
		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			webElement = wait.until(ExpectedConditions.elementToBeClickable(webElement));
			if (webElement != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public void moveToElementActions(By by) throws Exception {
		Actions actions = new Actions(driver);
		WebElement webElement = getWebElement(by);
		actions.moveToElement(webElement).build().perform();
	}

	/**
	 * Refresh Page
	 */
	public static void refreshPage() {
		((JavascriptExecutor) driver).executeScript("location.reload();");
		waitLoadPageComplete();
	}

	/**
	 * Close notification of the extension 'Salesforce Inspector'
	 *
	 * @throws Exception
	 */
	public static void closeNorificationSalesforceInspector(By elementTarget) throws Exception {
		String escape = Keys.chord(Keys.ESCAPE);
		driver.findElement(elementTarget).sendKeys(escape);
	}

	/**
	 * Switch to second tab, closes the first if boolean true
	 */
	public void switchToNewTab(boolean closeTab) {
		String oldTab = driver.getWindowHandle();
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		tabs.remove(oldTab);
		if (closeTab && tabs.size() > 0) {
			driver.close();
		}
		if (tabs.size() > 0) {
			driver.switchTo().window(tabs.get(0));
		}
	}

	/**
	 * Move to an element
	 *
	 * @param element Element to move to
	 */
	void moveToElement(By element) throws Exception {
		waitElement(element, MAX_SECONDS);
		WebElement webElement = getWebElement(element);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", webElement);
	}

	/**
	 * Scroll to the maximum height of the page
	 */
	void scrollToMaximumHeightOfPage() {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,document.body.scrollHeight)");
	}

	public static void scrollToDown(int repetitions) {
		int i = 0;
		int x = 100;
		while (i < repetitions) {
			JavascriptExecutor Scrool = (JavascriptExecutor) driver;
			Scrool.executeScript("window.scrollBy(0, arguments[0])", x);
			i++;
			x += 100;
		}
	}

	public static void scrollToDownUntilDisplayEleemnt(int repetitions, By finder) throws Exception {
		int i = 0;
		int x = 100;
		while (i < repetitions) {
			JavascriptExecutor Scrool = (JavascriptExecutor) driver;
			Scrool.executeScript("window.scrollBy(0, arguments[0])", x * (-1));
			if (isPresent(finder) && isDisplayed(finder)) {
				break;
			} else {
				TimeUnit.MILLISECONDS.sleep(500);
				i++;
				x += 100;
			}

		}
	}

	public void selectAll() throws AWTException {
		Actions builder = new Actions(driver);
		builder.sendKeys(Keys.chord(Keys.CONTROL, "a")).build().perform();
		builder.release().perform();
	}

	/**
	 * Get the Absolute xpath of a WebElement
	 *
	 * @param webElement WebElement to retrieve the absolute xpath
	 */
	String getAbsoluteXPathFromWebElement(By webElement) {
		return (String) ((JavascriptExecutor) driver).executeScript("function absoluteXPath(element) {"
				+ "var comp, comps = [];" + "var parent = null;" + "var xpath = '';"
				+ "var getPos = function(element) {" + "var position = 1, curNode;"
				+ "if (element.nodeType == Node.ATTRIBUTE_NODE) {" + "return null;" + "}"
				+ "for (curNode = element.previousSibling; curNode; curNode = curNode.previousSibling) {"
				+ "if (curNode.nodeName == element.nodeName) {" + "++position;" + "}" + "}" + "return position;" + "};"
				+ "if (element instanceof Document) {" + "return '/';" + "}"
				+ "for (; element && !(element instanceof Document); element = element.nodeType == Node.ATTRIBUTE_NODE ? element.ownerElement : element.parentNode) {"
				+ "comp = comps[comps.length] = {};" + "switch (element.nodeType) {" + "case Node.TEXT_NODE:"
				+ "comp.name = 'text()';" + "break;" + "case Node.ATTRIBUTE_NODE:"
				+ "comp.name = '@' + element.nodeName;" + "break;" + "case Node.PROCESSING_INSTRUCTION_NODE:"
				+ "comp.name = 'processing-instruction()';" + "break;" + "case Node.COMMENT_NODE:"
				+ "comp.name = 'comment()';" + "break;" + "case Node.ELEMENT_NODE:" + "comp.name = element.nodeName;"
				+ "break;" + "}" + "comp.position = getPos(element);" + "}"
				+ "for (var i = comps.length - 1; i >= 0; i--) {" + "comp = comps[i];"
				+ "xpath += '/' + comp.name.toLowerCase();" + "if (comp.position !== null) {"
				+ "xpath += '[' + comp.position + ']';" + "}" + "}" + "return xpath;"
				+ "} return absoluteXPath(arguments[0]);", getWebElement(webElement));
	}

	/**
	 * Get the xpath of a WebElement
	 *
	 * @param webElement WebElement to retrieve the xpath
	 */
	String getXpathFromWebElement(By webElement) {
		String[] xpath = getWebElement(webElement).toString().split(" xpath: ");
		return xpath[1].substring(0, xpath[1].length() - 1);
	}

	/**
	 * Increases or decreases chrome zoom Example: 1 = 100%, 1.5 = 150%, etc.
	 *
	 * @param zoomLevel The desired zoom level
	 */
	void changeChromeZoomLevel(Double zoomLevel) {
		((JavascriptExecutor) driver).executeScript("document.body.style.zoom = '" + zoomLevel + "'");
	}

	/**
	 * Get the second method that is being executed in runtime and return its name
	 */
	protected String getMethodName() {
		return Thread.currentThread().getStackTrace()[2].getMethodName();
	}

	/**
	 * Replace the ^ and & to Empty String for creating the log message
	 *
	 * @param log The log message
	 */
	private String replaceStartEndRegex(String log) {
		return log.replace(CIRCUNFLEJO, StringUtils.EMPTY).replace(DOLAR, StringUtils.EMPTY);
	}

	/**
	 * Create the log message based on cucumber annotation value, matching the
	 * current method that is being executed with the size of the object[] passed as
	 * parameters
	 *
	 * @param className  The class name
	 * @param methodName The method name
	 * @param objects    Array of objects containning the values to create the log
	 */
	protected String createLog(Class<?> className, String methodName, Object[] objects) throws Exception {
		Predicate<Method> isGetNameEqualsMethodName = e -> e.getName().equals(methodName);
		Predicate<Method> isParameterCountEqualsObjectsLength = e -> e.getParameterCount() == objects.length;
		Method method = Arrays.stream(className.getMethods())
				.filter(isGetNameEqualsMethodName.and(isParameterCountEqualsObjectsLength)).findFirst()
				.orElseThrow(Exception::new);
		String logValue = getAnnotationValue(method);
		logValue = replaceStartEndRegex(logValue);
		String timestamp = new Timestamp(System.currentTimeMillis()) + " - ";
		for (int i = 0; i < method.getParameterCount(); i++) {
			if (objects[i].toString().contains("\n")) {
				logValue += "\n\"\"\"" + StringUtils.repeat(" ", timestamp.length()) + objects[i] + "\n"
						+ StringUtils.repeat(" ", timestamp.length()) + "\"\"\"";
			} else {
				logValue = logValue.replaceFirst(REGEX, "[" + objects[i] + "]");
			}
		}
		return logValue;
	}

	/**
	 * Create the log message based on cucumber annotation value, matching the
	 * current method that is being executed
	 *
	 * @param className  The class name
	 * @param methodName The method name
	 */
	String createLog(Class<?> className, String methodName) throws Exception {
		Predicate<Method> isGetNameEqualsMethodName = e -> e.getName().equals(methodName);
		Method method = Arrays.stream(className.getMethods()).filter(isGetNameEqualsMethodName).findFirst()
				.orElseThrow(Exception::new);
		String logValue = getAnnotationValue(method);
		logValue = replaceStartEndRegex(logValue);
		return logValue;
	}

	/**
	 * Get the annotation value of a method
	 *
	 * @param method The object of the method
	 * @throws Exception
	 */
	private static String getAnnotationValue(Method method) throws Exception {
		if (method.isAnnotationPresent(Given.class)) {
			return method.getAnnotation(Given.class).value();
		} else if (method.isAnnotationPresent(When.class)) {
			return method.getAnnotation(When.class).value();
		} else if (method.isAnnotationPresent(And.class)) {
			return method.getAnnotation(And.class).value();
		} else if (method.isAnnotationPresent(Then.class)) {
			return method.getAnnotation(Then.class).value();
		} else if (method.isAnnotationPresent(Dado.class)) {
			return method.getAnnotation(Dado.class).value();
		} else if (method.isAnnotationPresent(Quando.class)) {
			return method.getAnnotation(Quando.class).value();
		} else if (method.isAnnotationPresent(E.class)) {
			return method.getAnnotation(E.class).value();
		} else if (method.isAnnotationPresent(Então.class)) {
			return method.getAnnotation(Então.class).value();
		} else if (method.isAnnotationPresent(Entao.class)) {
			return method.getAnnotation(Entao.class).value();
		} else
			throw new Exception("Anotação do cucumber inesperada");
	}

	public static void scrollByHeightPage(int height) {
		JavascriptExecutor Scrool = (JavascriptExecutor) driver;
		Scrool.executeScript("window.scrollBy(0," + height + ")", "");
	}

	public static void validateAndScrollToElement(By element) throws Exception {
		if (!waitVisibilityAndPresenceOfElement(element, MAX_SECONDS) && !isDisplayed(element)) {
			throw new Exception("This element was not found: " + element.toString());
		}
		scrollToElement(element);
	}

	public static void clearFieldActions(By by) {
		getWebElement(by).sendKeys(Keys.ESCAPE);
		getWebElement(by).sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
	}

	public static void waitAndScrollToElement(By element, int timeout) throws Exception {
		waitElement(element, timeout);
		if (!isPresent(element)) {
			throw new Exception("This element was not found: " + element.toString());
		}
		scrollToElementAlternative(element);
	}

	public static void scrollToElementBottom(By element) throws Exception {
		WebElement webElement = getWebElement(element);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", webElement);
	}

	/**
	 * drag and drop an element
	 *
	 * @param elementBase
	 * @param target
	 * @throws ElementNotFoundException
	 * @throws InterruptedException
	 * @throws AWTException
	 */
	public void dragAndDrop(By elementBaseLocator, By targetLocator)
			throws Exception, AWTException, InterruptedException {
		WebElement elementBase = getWebElement(elementBaseLocator);
		WebElement target = getWebElement(targetLocator);
		Actions action = new Actions(driver);
		action.clickAndHold(elementBase).moveToElement(target).release().build().perform();
	}

	/**
	 * Return "true" if the element is enabled, return false if not
	 *
	 * @param element element
	 */
	public static Boolean isEnabled(By element) throws Exception {
		return getWebElement(element).getAttribute("disabled") == null;
	}

	/**
	 * Return true if the element is present and displayed, return false if not
	 *
	 * @param element
	 * @return
	 * @throws Exception
	 */
	public static Boolean isDisplayed(By element) throws Exception {
		if (isPresent(element)) {
			if ((boolean) getWebElement(element).isDisplayed()) {
				return true;
			}
		}
		return false;
	}

	public void clickWithActionsJS(By element) throws Exception {
		Actions actions = new Actions(driver);
		WebElement webElement = getWebElement(element);
		actions.moveToElement(webElement).click().build().perform();
	}

	public static List<By> getAllLocatorsBy(String xpath) throws Exception {
		List<By> locators = new LinkedList<By>();
		int index = 1;
		while (index <= DriverFactory.getDriver().findElements(By.xpath(xpath)).size()) {
			locators.add(By.xpath("(" + xpath + ")[" + index + "]"));
			index++;
		}
		return locators;
	}

	/**
	 * Executes the javascript action that performs scrolling for the element with
	 * the element in the middle of the screen
	 *
	 * @param element Element
	 */
	public static void scrollToElementAlternative(By element) throws Exception {
		WebElement webElement = getWebElement(element);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", webElement);
	}

	/**
	 * Executes the javascript action that performs scrolling for the element with
	 * the element in the middle of the screen
	 *
	 * @param element Element
	 */
	public static void scrollToElementInTheMiddle(By element) throws Exception {
		boolean failed = false;
		int tryCount = 0;
		do {
			try {
				String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
						+ "var elementTop = arguments[0].getBoundingClientRect().top;"
						+ "window.scrollBy(0, elementTop-(viewPortHeight/2));";
				((JavascriptExecutor) driver).executeScript(scrollElementIntoMiddle, getWebElement(element));
				failed = false;
			} catch (StaleElementReferenceException e) {
				failed = true;
				tryCount++;
			}
			TimeUnit.MILLISECONDS.sleep(500);
		} while (failed && tryCount < 3);
	}

	public static void scrollToPositionElement(By by) {
		WebElement webElement = getWebElement(by);
		Rectangle rectangle = webElement.getRect();
		int x = rectangle.getY();
		int y = rectangle.getY();
		((JavascriptExecutor) driver).executeScript("window.scrollTo(" + x + ", " + y + ")");
	}

	/**
	 * Scroll page using pageUp
	 *
	 * @param repeticoes
	 * @throws AWTException
	 * @throws InterruptedException
	 */
	public void scrollToUp(int repeticoes) throws AWTException, InterruptedException {
		Actions builder = new Actions(driver);
		org.openqa.selenium.interactions.Action seriesOfActions;
		int i = 1;
		while (i <= repeticoes) {
			seriesOfActions = builder.sendKeys(Keys.PAGE_UP).build();
			seriesOfActions.perform();
			i++;
		}
	}

	public void scrollToDownActions(int repeticoes) throws AWTException, InterruptedException {
		Thread.sleep(3500);
		Actions builder = new Actions(driver);
		org.openqa.selenium.interactions.Action seriesOfActions;
		int i = 1;
		while (i <= repeticoes) {
			seriesOfActions = builder.sendKeys(Keys.PAGE_DOWN).build();
			seriesOfActions.perform();
			i++;
		}
	}

	public static void hover(By element) {
		WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(element));
		Actions builder = new Actions(driver);
		builder.moveToElement(getWebElement(element)).perform();
	}

	public void waitInvisibilityOfElement(By locator) {
		WebDriverWait wait = new WebDriverWait(driver, MAX_SECONDS);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public void pasteText() throws AWTException {
		Actions actions = new Actions(driver);
		actions.keyDown(Keys.CONTROL).sendKeys("v").keyUp(Keys.CONTROL).build().perform();
	}

	public LinkedHashMap<String, String> getLinkedHashMapColumn(String column, String filter,
			LinkedHashMap<String, String> textClipboard) throws UnsupportedFlavorException, IOException {
		LinkedHashMap<String, String> listFilterClipboard = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> listFieterClipboardOrdenedKeys = new LinkedHashMap<String, String>();
		listFilterClipboard = textClipboard.entrySet().stream()
				.filter(m -> m.getValue().contains(column) || m.getValue().contains(filter))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
		int n = 0;
		for (String Key : listFilterClipboard.keySet()) {
			listFieterClipboardOrdenedKeys.put("celula" + n++, listFilterClipboard.get(Key));
		}
		return listFieterClipboardOrdenedKeys;
	}

	public LinkedHashMap<String, String> getClipboardToMap() throws UnsupportedFlavorException, IOException {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		String c = String.valueOf(clipboard.getData(DataFlavor.stringFlavor));
		String[] texto = c.replace(" ", "").replace("\n", "\t").replace("\"", "").split("\t");
		LinkedHashMap<String, String> textClipboard = new LinkedHashMap<String, String>();
		int i = 1;
		for (String string : texto) {
			if (!string.contains("Id")) {
				textClipboard.put("info" + i, string);
				i++;
			}
		}
		return textClipboard;
	}

	public void setStringToClipboard(StringBuilder columnsToclipboard) throws UnsupportedFlavorException, IOException {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection stringSelection = new StringSelection(columnsToclipboard.toString());
		clipboard.setContents(stringSelection, null);
	}

	public void setTextToClipboardFromBrowserJS(String text) throws Exception {
		String scriptJS = "let textarea = document.createElement('textarea');\r\n"
				+ "textarea.style = \"position: absolute; left: -1000px; top: -1000px\";\r\n"
				+ "textarea.setAttribute('id', 'clip-textarea-hidden');\r\n" + "textarea.textContent = '"
				+ text.replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r") + "';\r\n"
				+ "document.body.appendChild(textarea);\r\n" + "textarea.select();";
		((JavascriptExecutor) driver).executeScript(scriptJS);
		justSendKeys(By.id("clip-textarea-hidden"), Keys.chord(Keys.CONTROL, "c"), false);
	}

	public void setStringToClipboard(String columnsToclipboard) throws UnsupportedFlavorException, IOException {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection stringSelection = new StringSelection(columnsToclipboard);
		clipboard.setContents(stringSelection, null);
	}

	public static String getClipboardAsString() throws HeadlessException, UnsupportedFlavorException, IOException {
		return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
	}

	public XSSFWorkbook createWorkbook(String fileName) throws IOException {
		File excelFile = new File(
				System.getProperty("user.dir") + File.separator + "excel" + File.separator + fileName);
		FileInputStream fis = new FileInputStream(excelFile);
		return new XSSFWorkbook(fis);
	}

	public LinkedHashMap<String, String> getCellColumnValueInXlsx(Sheet sheet, int columnIndex, int startLine,
			int lineLimit) throws IOException {
		int i = startLine;
		LinkedHashMap<String, String> cellValue = new LinkedHashMap<String, String>();
		while (i <= lineLimit) {
			Row headerRow = sheet.getRow(i);
			Cell cell = headerRow.getCell(columnIndex);
			cellValue.put("row" + i, cell.getStringCellValue());
			i++;
		}
		return cellValue;
	}

	public void writeListInExcelXlsx(Workbook workbook, LinkedHashMap<String, String> listMap, String sheetName,
			int columnIndex, int startLine) throws IOException, UnsupportedFlavorException {
		SimpleDateFormat df = new SimpleDateFormat("ddMMYYHHmm");
		Date data = new Date();
		Sheet sheet = workbook.getSheet(sheetName);
		String name = String.valueOf(df.format(data) + "Automação");
		int row = startLine;
		for (String key : listMap.keySet()) {
			Row headerRow = sheet.getRow(row);
			Cell cell = headerRow.createCell(columnIndex);
			String incremental = String.format("%04d", row);
			if (!listMap.containsValue("Name") && !listMap.containsValue(name + incremental)) {
				cell.setCellValue(listMap.get(key));
			} else {
				if (row == 0) {
					cell.setCellValue(listMap.get(key));
				} else {
					cell.setCellValue(name + incremental);
				}
			}
			row++;
		}
	}

	public void saveWorkbook(XSSFWorkbook workbook, String fileOutName) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(
				System.getProperty("user.dir") + File.separator + "excel" + File.separator + fileOutName);
		workbook.write(fileOut);
		workbook.close();
	}

	// /**
	// * get current Url
	// *
	// * @return
	// */
	// public String getUrlCurrentPage() {
	// return EFASingleton.getInstance().cv_driver.getCurrentUrl();
	// }
	public String getCellValuesInSpreadsheet(File spreadsheet, String sheetName) throws IOException {
		FileInputStream fis = new FileInputStream(spreadsheet);
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet insertCaseMassSheet = workbook.getSheet(sheetName);
		int rowsNumber = insertCaseMassSheet.getPhysicalNumberOfRows();
		int cellsNumber = insertCaseMassSheet.getRow(0).getPhysicalNumberOfCells();
		int rowCount = 0;
		StringBuilder insertCaseMassSheetStr = new StringBuilder();
		while (rowsNumber > 0) {
			cellsNumber = insertCaseMassSheet.getRow(rowCount).getPhysicalNumberOfCells();
			int cellCount = 0;
			Row row = insertCaseMassSheet.getRow(rowCount);
			while (cellsNumber > 0) {
				Cell cell = row.getCell(cellCount);
				insertCaseMassSheetStr.append(cell.getStringCellValue());
				if (cellsNumber == 1) {
					if (rowCount != (insertCaseMassSheet.getPhysicalNumberOfRows() - 1)) {
						insertCaseMassSheetStr.append("\n");
					}
				} else {
					insertCaseMassSheetStr.append("\t");
				}
				cellsNumber--;
				cellCount++;
			}
			rowsNumber--;
			rowCount++;
		}
		workbook.close();
		return insertCaseMassSheetStr.toString();
	}

	public static int getRowNumbersInSpreadheet(File spreadsheet, String sheetName) throws IOException {
		FileInputStream fis = new FileInputStream(spreadsheet);
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet insertCaseMassSheet = workbook.getSheet(sheetName);
		workbook.close();
		return insertCaseMassSheet.getPhysicalNumberOfRows();
	}

	public static int getCellNumbersInSpreadheet(File spreadsheet, String sheetName) throws IOException {
		FileInputStream fis = new FileInputStream(spreadsheet);
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet insertCaseMassSheet = workbook.getSheet(sheetName);
		workbook.close();
		return insertCaseMassSheet.getRow(0).getPhysicalNumberOfCells();
	}

	/**
	 * Return the first selected option of a comboBox
	 *
	 * @param comboBox
	 * @throws Exception
	 */
	public String getFirstSelecedOptionComboBox(By comboBox) throws Exception {
		WebElement comboBoxElement = getWebElement(comboBox);
		Select combo = new Select(comboBoxElement);
		try {
			return combo.getFirstSelectedOption().getText();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<WebElement> getOptionsComboBox(By comboBoxLocator) throws Exception {
		WebElement comboBoxElement = getWebElement(comboBoxLocator);
		Select combo = new Select(comboBoxElement);
		List<WebElement> options;
		try {
			options = combo.getOptions();
		} catch (Exception e) {
			throw new NoSuchElementException("Opções do combo não foram encontradas!");
		}
		return options;
	}

	public static void writeInXlsxFile(File spreadsheet, String sheetName, int rowIndex, int columnIndex, String value)
			throws IOException {
		FileInputStream fis = new FileInputStream(spreadsheet);
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheet(sheetName);
		Row row = sheet.getRow(rowIndex);
		Cell cell = row.getCell(columnIndex);
		cell.setCellValue(value);
		workbook.write(new FileOutputStream(spreadsheet));
		workbook.close();
		fis.close();
	}

	public static String getCellValueInXlsxFile(File spreadsheet, String sheetName, int rowIndex, int columnIndex)
			throws IOException {
		FileInputStream fis = new FileInputStream(spreadsheet);
		Workbook workbook = new XSSFWorkbook(fis);
		Sheet sheet = workbook.getSheet(sheetName);
		Row row = sheet.getRow(rowIndex);
		Cell cell = row.getCell(columnIndex);
		String cellValue = cell.getStringCellValue();
		workbook.close();
		fis.close();
		return cellValue;
	}

	public static String getClipboadValueAsString() throws UnsupportedFlavorException, IOException {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		return clipboard.getData(DataFlavor.stringFlavor).toString();
	}

	public static List<String> getColumnsInXlsx(File spreadsheet, String sheetName)
			throws FileNotFoundException, IOException {
		Workbook workbook = new XSSFWorkbook(new FileInputStream(spreadsheet));
		Sheet promocaoSaudeSheet = workbook.getSheet(sheetName);
		int promocaoSaudeCellNumbers = getCellNumbersInSpreadheet(spreadsheet, promocaoSaudeSheet.getSheetName());
		List<String> columnNames = new ArrayList<String>();
		int i = 0;
		while (i < promocaoSaudeCellNumbers) {
			Cell cell = promocaoSaudeSheet.getRow(0).getCell(i);
			columnNames.add(cell.getRichStringCellValue().toString());
			i++;
		}
		workbook.close();
		return columnNames;
	}

	public static void sleepBySystemTime(int milliseconds) throws InterruptedException {
		long currentTimeMillis = System.currentTimeMillis();
		while (System.currentTimeMillis() < currentTimeMillis + milliseconds) {
			Thread.sleep(1);
		}
	}
}
