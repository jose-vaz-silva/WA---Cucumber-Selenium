package com.web.automation.util;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UtilitiesWindows {
	
	/**
	 * Get the IP from your machine
	 */
	public static String getIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			return "Error to take the IP";
		}
	}
	
	/**
	 * close a process in the parameter
	 */
	public static void closeProcess(String nameProcess) throws InterruptedException, IOException {
		Runtime.getRuntime().exec("taskkill /F /IM " + nameProcess);
	}
	
	/**
	 * Create a new folder
	 *
	 * @param path
	 */
	public static void createDirectories(String path) {
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
	}
	
	/**
	 * Store in the clipboard a data to copy and paste
	 */
	public static void setClipboardData(String string) {
		// StringSelection is a class that can be used for copy and paste operations.
		StringSelection stringSelection = new StringSelection(string);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}
	
	/**
	 * Upload a file in the path in the parameter
	 */
	public static void uploadFile(String fileLocation) {
		try {
			// Setting clipboard with file location
			setClipboardData(fileLocation);
			// native key strokes for CTRL, V and ENTER keys
			Robot robot = new Robot();
			Thread.sleep(1000);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	
	/**
	 * Upload a the default file FileToUpoad.pdf
	 */
	public static void uploadFile() {
		try {
			String FilePath = new File("data").getAbsolutePath() + "\\" + "FileToUpload" + ".pdf";
			// Setting clipboard with file location
			setClipboardData(FilePath);
			// native key strokes for CTRL, V and ENTER keys
			Robot robot = new Robot();
			Thread.sleep(1000);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
