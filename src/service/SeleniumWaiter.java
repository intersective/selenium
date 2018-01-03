package service;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Throwables;
import common.BuildConfig;
import common.ElementContentPresence;
import common.ElementPresence;
import common.ElementType;
import common.ElementsPresence;
import common.EnclosedElementPresence;
import common.EnclosedElementsPresence;
import common.JQueryLoad;
import common.JsLoad;
import common.PopUpWindowPresence;


public class SeleniumWaiter {

	private final WebDriver driver;

	public SeleniumWaiter(WebDriver driver) {
		super();
		this.driver = driver;
	}

	public WebElement waitForElement(String locatorname, ElementType type, int timeoutInSeconds) {
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeoutInSeconds, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(org.openqa.selenium.NoSuchElementException.class);
			return wait.until(new ElementPresence(locatorname, type));
		} catch (org.openqa.selenium.TimeoutException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
		return null;
	}
	
	/**
	 * use for an element which encloses in a given element
	 * @param we
	 * @param locatorname
	 * @param type
	 * @param timeoutInSeconds
	 * @return
	 */
	public WebElement waitForElement(WebElement we, String locatorname, ElementType type, int timeoutInSeconds) {
		try {
			Wait<WebElement> wait = new FluentWait<WebElement>(we).withTimeout(timeoutInSeconds, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(org.openqa.selenium.NoSuchElementException.class);
			return wait.until(new EnclosedElementPresence(locatorname, type));
		} catch (org.openqa.selenium.TimeoutException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
		return null;
	}
	
	public WebElement waitForElement(String locatorname) {
		return waitForElement(locatorname, ElementType.CSSSELECTOR, BuildConfig.elementWaitTime);
	}
	
	public WebElement waitForElement(String locatorname, ElementType type) {
		return waitForElement(locatorname, type, BuildConfig.elementWaitTime);
	}
	
	public WebElement waitForElement(WebElement we, String locatorname) {
		return waitForElement(we, locatorname, ElementType.CSSSELECTOR, BuildConfig.elementWaitTime);
	}
	
	public WebElement waitForElement(WebElement we, String locatorname, ElementType type) {
		return waitForElement(we, locatorname, type, BuildConfig.elementWaitTime);
	}
	
	public List<WebElement> waitForElements(String locatorname, ElementType type, int timeoutInSeconds) {
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeoutInSeconds, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(org.openqa.selenium.NoSuchElementException.class);
			return wait.until(new ElementsPresence(locatorname, type));
		} catch (org.openqa.selenium.TimeoutException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
		return null;
	}
	
	/**
	 * use for the elements which encloses in a given element
	 * @param we
	 * @param locatorname
	 * @param type
	 * @param timeoutInSeconds
	 * @return
	 */
	public List<WebElement> waitForElements(WebElement we, String locatorname, ElementType type, int timeoutInSeconds) {
		try {
			Wait<WebElement> wait = new FluentWait<WebElement>(we).withTimeout(timeoutInSeconds, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(org.openqa.selenium.NoSuchElementException.class);
			return wait.until(new EnclosedElementsPresence(locatorname, type));
		} catch (org.openqa.selenium.TimeoutException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
		return null;
	}
	
	public List<WebElement> waitForElements(String locatorname) {
		return waitForElements(locatorname, ElementType.CSSSELECTOR, BuildConfig.elementWaitTime);
	}
	
	public List<WebElement> waitForElements(String locatorname, ElementType type) {
		return waitForElements(locatorname, type, BuildConfig.elementWaitTime);
	}
	
	public List<WebElement> waitForElements(WebElement we, String locatorname) {
		return waitForElements(we, locatorname, ElementType.CSSSELECTOR, BuildConfig.elementWaitTime);
	}
	
	public List<WebElement> waitForElements(WebElement we, String locatorname, ElementType type) {
		return waitForElements(we, locatorname, type, BuildConfig.elementWaitTime);
	}
	
	public List<WebElement> waitForListContent(String locator, ElementType type, int timeoutInSeconds) {
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(timeoutInSeconds, TimeUnit.SECONDS).pollingEvery(1, TimeUnit.SECONDS)
					.ignoring(org.openqa.selenium.NoSuchElementException.class);
			if (wait.until(new ElementContentPresence(locator, type))) {
				return wait.until(new ElementsPresence(locator, type)); 
			}
			return null;
		} catch (org.openqa.selenium.TimeoutException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
		return null;
	}
	
	public List<WebElement> waitForListContent(String locator) {
		return waitForListContent(locator, ElementType.CSSSELECTOR, BuildConfig.elementWaitTime);
	}
	
	public List<WebElement> waitForListContent(String locator, ElementType type) {
		return waitForListContent(locator, type, BuildConfig.elementWaitTime);
	}
	
	public boolean waitForDocumentReady(int timeoutInSeconds) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
			return wait.until(new JQueryLoad()) && wait.until(new JsLoad());
		} catch (Exception e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
		return true;
	}
	
	public Boolean waitForPopUpWindow(int timeoutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		return wait.until(new PopUpWindowPresence());
	}
	
}
