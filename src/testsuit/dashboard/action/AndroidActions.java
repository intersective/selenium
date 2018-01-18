package testsuit.dashboard.action;


import org.openqa.selenium.WebDriver;

import service.SeleniumWaiter;
import service.TestLogger;
import service.Tools;

import com.google.common.base.Throwables;
import common.BuildConfig;


public class AndroidActions extends MobileActions {

	@Override
	public String getUserPoints(SeleniumWaiter sw) {
		return super.getUserPoints(sw);
	}

	@Override
	public WebDriver handleFileUpload(WebDriver driver, SeleniumWaiter sw,
			String mainWindowHandle) {
		for (String activeHandle : driver.getWindowHandles()) {
			if (!activeHandle.equals(mainWindowHandle)) {
				TestLogger.trace(String.format("switch to %s", activeHandle));
	            driver.switchTo().window(activeHandle);// switch to the pop-up window which contains a file upload button
	            break;
	        }
	    }
		return driver;
	}

	@Override
	public void selectFile(SeleniumWaiter sw, String filePath) {
		try {
			sw.waitForElement("input[type=file]").sendKeys(new String[] { BuildConfig.androidEvidenceFile });
			Tools.forceToWait(BuildConfig.jsWaitTime);
		} catch (Exception e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
	}

}
