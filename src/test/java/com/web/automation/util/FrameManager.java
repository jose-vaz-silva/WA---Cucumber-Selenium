package com.web.automation.util;

import static com.web.automation.constants.TimeOutConstants.MIN_SECONDS;
import static com.web.automation.core.Hooks.webDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.web.automation.constants.TimeOutConstants;
import com.web.automation.core.SeleniumCommands;

public class FrameManager extends SeleniumCommands {
	public static int currentFrameX = 0;
	public static int currentFrameY = 0;

	/**
	 * Given an element, move the Selenium webDriver to the corresponding frame.
	 * 
	 * @param element Element that will be manipulated.
	 * @throws ElementNotFoundException Exception that can be throw in case of the
	 *                                  Element is not found
	 */
	public static void findFrame(By element) throws Exception {
		SeleniumCommands.waitLoadPageComplete();
		boolean result = false;
		Integer count = 0;
		Integer MAX_WAIT = 3;
		count = 0;
		while (!waitVisibilityAndPresenceOfElement(element, MAX_WAIT)) {
			try {
				moveToFrame(count);
				if (isDisplayed(element)) {
					scrollToElement(element);
					result = true;
					break;
				} else {
					moveToFrame(null);
				}
			} catch (Exception e) {
				result = false;
			}
			count++;
			if (count == 10 && result == false) {
				throw new Exception("This element was not found: " + element.toString());
			}
			TimeUnit.MILLISECONDS.sleep(500);
		}
	}

	public static void findFrameRecursive(By element) throws Exception {
		if (waitPresenceOfElement(element, TimeOutConstants.MAX_SECONDS) && isDisplayed(element)) {
			scrollToElement(element);
			return;
		}
		List<WebElement> iframes = webDriver.findElements(By.xpath("//iframe | //frame"));
		int oldX = currentFrameX;
		int oldY = currentFrameY;
		for (WebElement iframe : iframes) {
			try {
				JavascriptExecutor js = (JavascriptExecutor) webDriver;
				js.executeScript("arguments[0].scrollIntoViewIfNeeded(true);", iframe);
				Thread.sleep(500);
				String ySTR = js.executeScript("return arguments[0].getBoundingClientRect().top", iframe).toString();
				String xSTR = js.executeScript("return arguments[0].getBoundingClientRect().left", iframe).toString();
				currentFrameX = oldX
						+ Integer.parseInt(xSTR.contains(".") ? xSTR.substring(0, xSTR.indexOf(".")) : xSTR);
				currentFrameY = oldY
						+ Integer.parseInt(ySTR.contains(".") ? ySTR.substring(0, ySTR.indexOf(".")) : ySTR);
			} catch (Exception e) {
				System.out.println("Error findFrameRecu");
				continue;
			}
			webDriver.switchTo().frame(iframe);
			try {
				findFrameRecursive(element);
				return;
			} catch (Exception e) {
				continue;
			}
		}
		currentFrameX = oldX;
		currentFrameY = oldY;
		webDriver.switchTo().parentFrame();
		throw new Exception("Element not found");
	}

	/**
	 * Moves to the outermost frame of the system
	 */
	public static void moveToRootFrame() {
		moveToFrame(null);
	}

	/**
	 * Selects the frame reported by the "index" parameter
	 * 
	 * @param index index of the frame
	 */
	private static void moveToFrame(Integer index) {
		if (index == null) {
			webDriver.switchTo().defaultContent();
			currentFrameX = 0;
			currentFrameY = 0;
		} else {
			webDriver.switchTo().frame(index);
		}
	}

	public static void findAndMoveToElementInsideFrames(By locator) throws Exception {
		SeleniumCommands.waitLoadPageComplete();
		webDriver.switchTo().defaultContent();
		int count = 0;
		try {
			waitVisibilityAndPresenceOfElement(By.xpath("(//iframe | //frame)[1]"), MIN_SECONDS);
			List<WebElement> frames = webDriver.findElements(By.xpath("//iframe | //frame"));
			for (WebElement frame : frames) {
				WebDriverWait wait = new WebDriverWait(webDriver, 10);
				wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));
				webDriver.switchTo().frame(frame);
				for (int i = 0; i < 6; i++) {
					if (isPresent(locator)) {
						break;
					}
					if (i == 5)
						throw new Exception("Elemento: '" + locator + "' não foi encontrado");
					TimeUnit.MILLISECONDS.sleep(500);
				}
			}
		} catch (Exception e) {
			if (count >= 5) {
				throw new Exception(e);
			}
		}
		TimeUnit.SECONDS.sleep(1);
		count++;
	}

	public static void findFramesAndMovetoElementAlt(By locator) throws Exception {
		SeleniumCommands.waitLoadPageComplete();
		int count = 0;
		try {
			// while () {
			// webDriver.switchTo().frame(frameIndex);
			if (isPresent(locator)) {
				// break;
			}
			// }
		} catch (Exception e) {
			if (count >= 5) {
				throw new Exception(e);
			}
		}
		TimeUnit.SECONDS.sleep(1);
		count++;
	}

	public static void moveToFrameElement(By frame) {
		webDriver.switchTo().defaultContent();
		webDriver.switchTo().frame(SeleniumCommands.getWebElement(frame));
	}

	/**
	 * Selects the frame reported by the "webElement" parameter
	 * 
	 * @param locator WebElement of the frame
	 */
	private static void moveToFrameWebElement(By locator) throws Exception {
		WebElement frameElement = getWebElement(locator);
		if (locator == null) {
			webDriver.switchTo().defaultContent();
			currentFrameX = 0;
			currentFrameY = 0;
		} else {
			webDriver.switchTo().frame(frameElement);
		}
	}

	/**
	 * Given an element, move the Selenium driver to the corresponding frame.
	 * 
	 * @param element      Element that will be manipulated.
	 * @param frameLocator Frame that will be directed.
	 * @throws ElementNotFoundException Exception that can be throw in case of the
	 *                                  Element is not found
	 */
	public static void findFrameWebElement(By element, By frameLocator) throws Exception {
		boolean result;
		Integer tries = 0;
		while (!isPresent(element)) {
			// count = 0;
			while (true) {
				try {
					moveToFrameWebElement(frameLocator);
					Integer MAX_WAIT = 15;
					if (!waitElement(element, MAX_WAIT)) {
						SeleniumCommands.refreshPage();
						moveFrameAndClick(frameLocator);
						moveToFrameWebElement(frameLocator);
					}
					if (waitPresenceOfElement(element, MAX_WAIT) && isPresent(element)) {
						scrollToElement(element);
						result = true;
						break;
					} else {
						moveToFrameWebElement(null);
					}
				} catch (Exception e) {
					result = false;
					break;
				}
				// count++;
			}
			if (result) {
				return;
			} else {
				Integer MAX_TRIES = 5;
				tries++;
				Thread.sleep(1000);
				if (tries > MAX_TRIES) {
					throw new Exception("This element was not found: " + element.toString());
				}
			}
		}
	}
}
