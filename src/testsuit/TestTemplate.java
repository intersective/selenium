package testsuit;


import java.util.List;
import java.util.concurrent.CompletableFuture;

import listener.TestResultListener;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import respositry.LocalStore;
import service.AddCaseResultTask;
import service.SeleniumWaiter;
import service.ShareWebDriver;
import service.TestLogger;
import service.Tools;

import com.google.common.base.Throwables;
import common.BuildConfig;
import common.ElementType;
import common.ShareConfig;


@Listeners({TestResultListener.class})
public abstract class TestTemplate implements ITest {

	protected WebDriver driver;
	protected SeleniumWaiter sw;
	private String testname;
	private long start;
	
	@BeforeClass
	public void setup() {
		ShareWebDriver temp = ShareWebDriver.getInstance();
		driver = temp.getDriver();
		sw = temp.getSw();
		start = System.currentTimeMillis() / 1000;
	}
	
	public abstract void main();
	
	@AfterMethod
	public void afeter(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		long duration = (System.currentTimeMillis() / 1000) - start;
		TestLogger.trace(String.format("finishing %s - %s, %s", result.getInstanceName(), methodName, result.getMethod().getSuccessPercentage()));
		if (BuildConfig.testrail) {
			String runCaseId = LocalStore.getInstance().getValue(result.getInstanceName());
			if (runCaseId != null && ShareConfig.runId != null) {
				CompletableFuture.runAsync(new AddCaseResultTask(methodName,result.getThrowable(), duration, runCaseId, ShareConfig.runId));
			}
		}
	}

	protected void setname(String testname) {
		this.testname = testname;
	}

	@Override
	public String getTestName() {
		return testname;
	}
	
	protected Object runJSScript(String script) {
		if (driver instanceof JavascriptExecutor) {
			TestLogger.trace((String.format("js script: %s",script)));
			return ((JavascriptExecutor) driver).executeScript(script);
		}
		return null;
	}
	
	protected void waitForLoadFinished() {
		do {
			Tools.forceToWait(1);
		} while (runJSScript("return document.querySelector('ion-loading');") !=null);
	}

	protected WebElement findElement(String locator, ElementType type) {
		try {
			return driver.findElement(Tools.getBy(type, locator));
		} catch (org.openqa.selenium.WebDriverException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
			return null;
		}
	}
	
	protected WebElement findElement(String locator) {
		try {
			return driver.findElement(Tools.getBy(locator));
		} catch (org.openqa.selenium.WebDriverException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
			return null;
		}
	}
	
	protected WebElement findElement(WebElement parentElement, String locator) {
		return findElement(parentElement, locator, ElementType.CSSSELECTOR);
	}
	
	/**
	 * find an element which enclosed by a parent element
	 * @param parentElement
	 * @param locator
	 * @param type
	 * @return
	 */
	protected WebElement findElement(WebElement parentElement, String locator, ElementType type) {
		try {
			return parentElement.findElement(Tools.getBy(type, locator));
		} catch (org.openqa.selenium.WebDriverException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
		return null;
	}
	
	protected List<WebElement> findElements(String locator, ElementType type) {
		try {
			return driver.findElements(Tools.getBy(type, locator));
		} catch (org.openqa.selenium.WebDriverException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
			return null;
		}
	}
	
	protected List<WebElement> findElements(String locator) {
		try {
			return driver.findElements(Tools.getBy(locator));
		} catch (org.openqa.selenium.WebDriverException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
		return null;
	}
	
	/**
	 * it continuously checks the loading DIV pop up for a given seconds
	 * if the DIV block cannot find eventually or throws an exception, we treat the file has uploaded
	 * @param timeoutInSeconds
	 */
	protected void waitForFileUploading(int timeoutInSeconds) {
		try {
			while (sw.waitForElement("div[ng-show='!LVm.showLoading']", ElementType.CSSSELECTOR, timeoutInSeconds) != null) {
				TestLogger.trace("waiting for uploading");
				Tools.forceToWaitInMilli(100);
			}
		} catch (Exception e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		} finally {
			TestLogger.trace("finished uploading");
		}
	}
	
	/**
	 * it retrieves the desired element for a given seconds by scrolling
	 * it throws assertion error not if the element does not exist
	 * @param locator
	 * @return WebElement
	 */
	protected WebElement waitForVisibleWithScroll(String locator) {
		return waitForVisibleWithScroll(locator, ElementType.CSSSELECTOR);
	}
	
	protected WebElement waitForVisibleWithScroll(String locator, ElementType type) {
		WebElement webElement = sw.waitForElement(locator, type);
		Assert.assertNotNull(webElement);
		return scrollIfNotVisible(webElement);
	}
	
	protected WebElement scrollIfNotVisible(WebElement webElement) {
		if (BuildConfig.headless) {// we cannot detect the visibility at the headless mode
			scrollToElement(webElement);
			Tools.forceToWait(5);
		} else {
			if (!webElement.isDisplayed()) {
				scrollToElement(webElement);
				Tools.forceToWait(1);
			}
		}
		return webElement;
	}
	
	protected void scrollToElement(WebElement element) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.perform();
	}
	
}
