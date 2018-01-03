package testsuit.dashboard.action;


import java.awt.AWTException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import service.PageAction;
import service.SeleniumWaiter;
import service.TestLogger;
import service.Tools;
import service.UIAction;

import com.google.common.base.Throwables;
import common.ElementType;


public class Actions implements PageAction {

	public String getUserPoints(SeleniumWaiter sw) {
		List<WebElement> lis = sw.waitForElement("dashboard-data", ElementType.CLASSNAME).findElements(By.tagName("li"));
		WebElement points = lis.get(1).findElement(By.className("number"));
		Assert.assertNotNull(points);
		return Tools.getElementTextContent(points);
	}

	public WebDriver handelFileUpload(WebDriver driver, SeleniumWaiter sw, String mainWindowHandle) {
		driver.switchTo().frame("filepicker_dialog");
		WebElement chooseFile = sw.waitForElement("section.fp__drag-and-drop button");
		Assert.assertNotNull(chooseFile);
		chooseFile.click();
		return driver;
	}
	
	public void selectFile(SeleniumWaiter sw) throws Exception {
		try {
			UIAction.pasteAndEnter();
		} catch (AWTException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
			throw new Exception(e.getLocalizedMessage());
		}
	}
	
}
