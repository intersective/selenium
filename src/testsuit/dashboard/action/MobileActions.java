package testsuit.dashboard.action;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import service.SeleniumWaiter;
import service.TestLogger;


public class MobileActions extends Actions {

	@Override
	public String getUserPoints(SeleniumWaiter sw) {
		return super.getUserPoints(sw);
	}

	@Override
	public WebDriver handleFileUpload(WebDriver driver, SeleniumWaiter sw, String mainWindowHandle) {
		for (String activeHandle : driver.getWindowHandles()) {
			if (!activeHandle.equals(mainWindowHandle)) {
				TestLogger.trace(String.format("switch to %s", activeHandle));
	            driver.switchTo().window(activeHandle);// switch to the pop-up window which contains a file upload button
	            break;
	        }
	    }
		WebElement chooseFileBtn = sw.waitForElement("div.fp__content > div:nth-of-type(2) > div > section > div > button");
		Assert.assertNotNull(chooseFileBtn);
		chooseFileBtn.click();
		return driver;
	}

	@Override
	public void selectFile(SeleniumWaiter sw, String filePath) {
		super.selectFile(sw, filePath);
	}

}
