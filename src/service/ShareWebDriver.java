package service;


import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import common.BuildConfig;


public class ShareWebDriver {

	private final WebDriver driver;
	private final SeleniumWaiter sw;
	
	private static class ShareWebDriverHolder {
		private static final ShareWebDriver my = new ShareWebDriver();
	}
	
	public static ShareWebDriver getInstance() {
		return ShareWebDriverHolder.my;
	}
	
	private ShareWebDriver() {
		if (BuildConfig.headless) {
			DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
			capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, BuildConfig.webDriverPath);
			driver = new PhantomJSDriver(capabilities);
			driver.manage().window().maximize();
		} else {
			ChromeDriverService service = new ChromeDriverService.Builder()
			.usingDriverExecutable(new File(BuildConfig.webDriverPath))
			.usingAnyFreePort()
			.build();
			if (BuildConfig.mobile) {
				if (BuildConfig.androidDeviceSerial != null) {// real android device
					ChromeOptions chromeOptions = new ChromeOptions();
				    chromeOptions.setExperimentalOption("androidPackage", BuildConfig.androidPackage);
				    chromeOptions.setExperimentalOption("androidDeviceSerial", BuildConfig.androidDeviceSerial);
				    driver = new ChromeDriver(service, chromeOptions);
				} else {
					Map<String, Object> mobileEmulation = new HashMap<>();
					mobileEmulation.put("deviceName", BuildConfig.deviceName);
					ChromeOptions chromeOptions = new ChromeOptions();
					chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
					driver = new ChromeDriver(service, chromeOptions);
				}
			} else {
				DesiredCapabilities caps = DesiredCapabilities.chrome();
				LoggingPreferences logPrefs = new LoggingPreferences();
				logPrefs.enable(LogType.PERFORMANCE, Level.INFO);
				caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
				driver = new ChromeDriver(service , caps);
				driver.manage().window().maximize();
			}
		}
		
		sw = new SeleniumWaiter(driver);
	}
	
	public WebDriver getDriver() {
		return driver;
	}

	public SeleniumWaiter getSw() {
		return sw;
	}

	public void releaseResource() {
		int total = 0;
		Iterator<LogEntry> logEntries= driver.manage().logs().get(LogType.PERFORMANCE).iterator();
		while(logEntries.hasNext()) {
			total++;
			TestLogger.logNetwork(logEntries.next().getMessage());
		}
		TestLogger.logNetwork(String.format("%s %s log entries found", total, LogType.PERFORMANCE));
		if (driver != null) {
			driver.quit();
		}
	}
	
}
