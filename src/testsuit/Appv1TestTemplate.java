package testsuit;


import org.openqa.selenium.WebElement;

import service.TestLogger;
import service.Tools;

import com.google.common.base.Throwables;
import common.BuildConfig;
import common.ElementType;


public abstract class Appv1TestTemplate extends TestTemplate {

	@Override
	protected void waitForLoadFinished() {
		WebElement loading = sw.waitForElement(".loading-container");
		while (loading.getAttribute("className").indexOf("active") > 0){
			Tools.forceToWait(1);
			loading = sw.waitForElement(".loading-container");
		}
		Tools.forceToWait(BuildConfig.jsWaitTime);
	}

	@Override
	protected void waitForFileUploading(int timeoutInSeconds) {
		try {
			while (sw.waitForElement(".fsp-picker", ElementType.CSSSELECTOR, timeoutInSeconds) != null) {
				TestLogger.trace("fsp-picker waiting for uploading");
				Tools.forceToWaitInMilli(100);
			}
		} catch (Exception e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		} finally {
			TestLogger.trace("finished uploading");
		}
	}
	
	protected void waitForBackdropRemoved() {
		while (sw.waitForElement(".backdrop").getAttribute("class").contains("visible")) {
			Tools.forceToWait(1);
		}
	}
	
}
