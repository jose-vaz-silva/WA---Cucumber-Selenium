package com.web.automation.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.WinDef;
import com.web.automation.constants.TestRunnerConstants;
import com.web.automation.core.DriverFactory;
import com.web.automation.core.Hooks;
import com.web.automation.core.SeleniumCommands;
import com.web.automation.models.CucumberScenario;

public class GenerateEvidences {
	private static XWPFDocument doc;
	private static FileOutputStream out;
	public static int evidence_count;
	private static String disableEvidence = System.getProperty("disableEvidence") != null
			? System.getProperty("disableEvidence")
			: "false";

	/**
	 * Generate a doc file with the evidences of the test execution
	 * 
	 * @param testCase       name of the test case of the spreadsheet
	 * @param finalResult    final result of the spreadsheet
	 * @param expectedResult expected result of the spreadsheet
	 * @param executionTime  execution time of the script
	 */
	public static void saveEvidences(String testCase, String finalResult, String expectedResult, Long executionTime)
			throws Exception {
		if (!disableEvidence.toLowerCase().equals("true")) {
			try {
				String time = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
				String directory = testCaseDirectory(testCase);
				String saveDirectory = directory.split("evidence")[0];
				ArrayList<String> listImages = searchImages(directory);
//        listImages = sort(listImages);
				Collections.sort(listImages);
				String templateName = "templateCT.docx";
				File file = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
						+ File.separator + "resources" + File.separator + templateName);
				FileInputStream fis = new FileInputStream(file.getAbsolutePath());
				doc = new XWPFDocument(fis);
				XWPFParagraph p = doc.createParagraph();
				XWPFRun r1 = p.createRun();
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, new Integer(
						Math.toIntExact(TimeUnit.MILLISECONDS.convert(executionTime, TimeUnit.MILLISECONDS))));
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:sss");
				r1.setFontFamily("Calibri");
				r1.setFontSize(11);
				r1.setBold(true);
				r1.setText("Test Case: ");
				r1.setText(testCase);
				XWPFTable table = doc.createTable();
				XWPFTableRow tableRowOne = table.getRow(0);
				XWPFParagraph paragraphOne = tableRowOne.getCell(0).addParagraph();
				setRun(paragraphOne.createRun(), "Final Result", true);
				tableRowOne.createCell();
				paragraphOne = tableRowOne.getCell(1).addParagraph();
				setRun(paragraphOne.createRun(), "Expected Result", true);
				tableRowOne.createCell();
				paragraphOne = tableRowOne.getCell(2).addParagraph();
				setRun(paragraphOne.createRun(), "Execution Time", true);
				XWPFTableRow tableRowTwo = table.createRow();
				XWPFParagraph paragraphTwo = tableRowTwo.getCell(0).addParagraph();
				setRun(paragraphTwo.createRun(), finalResult, false);
				paragraphTwo = tableRowTwo.getCell(1).addParagraph();
				setRun(paragraphTwo.createRun(), expectedResult, false);
				doc.createParagraph();
				paragraphTwo = tableRowTwo.getCell(2).addParagraph();
				setRun(paragraphTwo.createRun(), sdf.format(cal.getTime()), false);
				paragraphTwo = doc.createParagraph();
				XWPFRun r = paragraphTwo.createRun();
				for (String img : listImages) {
					int format = XWPFDocument.PICTURE_TYPE_PNG;
					String imageDirectory = directory + "/" + img;
					r.addBreak();
					r.setFontFamily("Calibri");
					r.setFontSize(11);
					r.setText("Name of the image: " + img);
					r.addPicture(new FileInputStream(imageDirectory), format, imageDirectory, Units.toEMU(446),
							Units.toEMU(257));
					r.addBreak(BreakType.TEXT_WRAPPING);
				}
				XWPFHeaderFooterPolicy headerFooterPolicy = doc.getHeaderFooterPolicy();
				if (headerFooterPolicy == null)
					headerFooterPolicy = doc.createHeaderFooterPolicy();
				XWPFFooter footer = headerFooterPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);
				XWPFParagraph paragraphFooter = footer.createParagraph();
				paragraphFooter.setAlignment(ParagraphAlignment.LEFT);
				XWPFRun runFooter = paragraphFooter.createRun();
				runFooter.setFontSize(11);
				runFooter.setFontFamily("calibri");
				runFooter.setText("Automated test run in " + time);
				CTSectPr sectPr = doc.getDocument().getBody().getSectPr();
				if (sectPr == null)
					doc.getDocument().getBody().addNewSectPr();
				out = new FileOutputStream(
						saveDirectory + File.separator + CucumberScenario.getFeatureName() + ".docx");
				doc.write(out);
				System.err.println("Document with evidence saved successfully!");
			} catch (Exception e) {
				System.err.println("Error generating document of test case evidence " + testCase);
			} finally {
				if (out != null) {
					out.close();
				}
				if (doc != null) {
					doc.close();
				}
			}
		}
	}

	/**
	 * Insert properties in a paragraph
	 * 
	 * @param run  paragraph formatter
	 * @param text text to be written
	 * @param bold Variable to control if the text should be bold or not
	 */
	private static void setRun(XWPFRun run, String text, boolean bold) {
		run.setFontFamily("Calibri");
		run.setFontSize(11);
		run.setText(text);
		run.setBold(bold);
	}

	/**
	 * Search the output for the folder corresponding to the test case
	 * 
	 * @param testCase test case to be found
	 * @return string directory of the test case evidences folder
	 */
	private static String testCaseDirectory(String testCase) {
		ArrayList<String> evidencesList;
		ArrayList<String> casesList = new ArrayList<>();
		evidencesList = nameDirectory(testCase, "output");
		String directory;
		for (String timeEvidences : evidencesList) {
			casesList = nameDirectory("evidence", timeEvidences);
		}
		directory = casesList.get(0);
		return directory;
	}

	/**
	 * Search folders and subfolders in a directory
	 * 
	 * @param word      folder name to be searched
	 * @param directory directory to be searched
	 * @return list with folders and subfolders found
	 */
	private static ArrayList<String> nameDirectory(String word, String directory) {
		ArrayList<String> list = new ArrayList<>();
		try {
			File file = new File(directory);
			list = search(file, word, list);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Invalid Directory");
		}
		return list;
	}

	/**
	 * Search folders and subfolders in a directory
	 * 
	 * @param file source file to be searched
	 * @param word word used to search
	 * @param list list where the directories will be saved
	 * @return list with folders and subfolders found
	 */
	private static ArrayList<String> search(File file, String word, ArrayList<String> list)
			throws FileNotFoundException {
		if (file.isDirectory()) {
			File[] subFolders = file.listFiles();
			if (subFolders == null) {
				throw new FileNotFoundException("Subfolders not found");
			}
			for (File subFolder : subFolders) {
				list = search(subFolder, word, list);
				if (file.getName().equalsIgnoreCase(word))
					list.add(file.getAbsolutePath());
				else if (file.getName().contains(word))
					list.add(file.getAbsolutePath());
			}
		} else if (file.getName().equalsIgnoreCase(word))
			list.add(file.getAbsolutePath());
		else if (file.getName().contains(word))
			list.add(file.getAbsolutePath());
		return list;
	}

	/**
	 * Search images in a directory
	 * 
	 * @param directory directory used to search
	 * @return list of image names
	 */
	public static ArrayList<String> searchImages(String directory) throws FileNotFoundException {
		File files = new File(directory);
		File[] imgs = files.listFiles();
		ArrayList<String> imageList = new ArrayList<>();
		if (imgs == null) {
			throw new FileNotFoundException("Images not found");
		}
		for (File file : imgs) {
			if (file.isFile()) {
				imageList.add(file.getName());
			}
		}
		return imageList;
	}

//  /**
//   * Take a screenshot
//   * 
//   * @param name
//   *          name of the screenshot
//   */
//  public static void takeScreenshot(String name) throws Exception {
//    name = TextManager.stringTreatment(name);
//    evidence_count++;
//  }

	/**
	 * Take a screenshot
	 * 
	 * @param name    name of the screenshot
	 * @param element
	 * @throws Exception
	 */
	public static void takeScreenshot(String name, By... elements) throws Exception {
		if (!disableEvidence.toLowerCase().equals("true")) {
			name = TextManager.stringTreatment(name);
			evidence_count++;
			String id = String.format("%03d", evidence_count);
			// (evidence_count < 100 ? "0" : "") + (evidence_count < 10 ? "0" : "")
			String imagePath = Hooks.evidencesPath + File.separator + "evidence" + File.separator + id + "_" + name
					+ ".png";
			WebDriver driver = DriverFactory.getDriver();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String runHeadless = System.getProperty("runHeadless") != null ? System.getProperty("runHeadless") : "null";
			String runRemote = System.getProperty("runRemote") != null ? System.getProperty("runRemote") : "null";
			if (!runHeadless.toLowerCase().equals("true") && !runRemote.toLowerCase().equals("true")
					&& System.getProperty("os.name").toLowerCase().contains("windows")) {
				int tryCount = 0;
				while (tryCount < 10) {
					try {
						String[] rectarg = TestRunnerConstants.EVIDENCE_ARGS.split(" ");
						double perce = getScreenScale();
						WebDriverWait wait = new WebDriverWait(driver, 15);
						FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE),
								new File(imagePath), true);
						BufferedImage myPicture = ImageIO.read(new File(imagePath));
						Graphics2D g = (Graphics2D) myPicture.getGraphics();
						g.setStroke(new BasicStroke(Integer.parseInt(rectarg[3])));
						g.setColor(Color.red);
						for (By element : elements) {
							WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
							SeleniumCommands.scrollToElementPosition(element);
							int width = (int) (webElement.getSize().getWidth() * perce);
							int height = (int) (webElement.getSize().getHeight() * perce);
							Rectangle rectangle = webElement.getRect();
							int yRect = rectangle.getY();
							String ySTR = String.valueOf(yRect);
							String xSTR = js
									.executeScript("return arguments[0].getBoundingClientRect().left", webElement)
									.toString();
							int x = (int) ((Integer
									.parseInt(xSTR.contains(".") ? xSTR.substring(0, xSTR.indexOf(".")) : xSTR)
									+ FrameManager.currentFrameX) * perce);
							int y = (int) ((Integer
									.parseInt(ySTR.contains(".") ? ySTR.substring(0, ySTR.indexOf(".")) : ySTR)
									+ FrameManager.currentFrameY) * perce);
							g.drawRect(x, y, width, height);
						}
						g.dispose();
						ImageIO.write(myPicture, "PNG", new File(imagePath));
						break;
					} catch (Exception e) {
						System.out.println("Error highlighting (" + e.getMessage() + ") evidente " + name);
						tryCount++;
						TimeUnit.MILLISECONDS.sleep(500);
					}
				}
			} else {
				for (By element : elements) {
					WebElement webElement = driver.findElement(element);
					js.executeScript("arguments[0].setAttribute('style', 'border: 4px solid red;');", webElement);
					FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE), new File(imagePath),
							true);
				}
				for (By element : elements) {
					WebElement webElement = driver.findElement(element);
					js.executeScript("arguments[0].setAttribute('style', 'border: 0px solid red;');", webElement);
				}
			}
		}
	}

	public static double getScreenScale() {
		WinDef.HDC hdc = GDI32.INSTANCE.CreateCompatibleDC(null);
		if (hdc != null) {
			float actual = GDI32.INSTANCE.GetDeviceCaps(hdc, 10 /* VERTRES */);
			float logical = GDI32.INSTANCE.GetDeviceCaps(hdc, 117 /* DESKTOPVERTRES */);
			GDI32.INSTANCE.DeleteDC(hdc);
			if (logical != 0 && logical / actual >= 1) {
				return logical / actual;
			}
		}
		return (Toolkit.getDefaultToolkit().getScreenResolution() / 96.0f);
	}
}