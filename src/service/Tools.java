package service;


import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import common.ElementType;


public class Tools {

	private static Pattern numberP = Pattern.compile("\\s+[1-9][0-9]*\\s+");
	
	private Tools() {
	}
	
	public static HashMap<String,String> readProperties(String propertyFile) {
		FileInputStream fileInput = null;
		
		try {
			File file = new File(propertyFile);
			fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			
			Enumeration<Object> enuKeys = properties.keys();
			HashMap<String,String> result = new HashMap<String, String>();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);
				result.put(key, value);
			}
			
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInput != null) {
				try {
					fileInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static By getBy(ElementType tag, String locatorname) {
		switch(tag){
			case NAME:
				return By.name(locatorname);
			case ID:
				return By.id(locatorname);
			case TAGNAME:
				return By.tagName(locatorname);
			case LINKTEXT:
				return By.linkText(locatorname);
			case XPATH:
				return By.xpath(locatorname);
			case CLASSNAME:
				return By.className(locatorname);
			case CSSSELECTOR:
				return By.cssSelector(locatorname);
			case PARTIALLINKTEXT:
				return By.partialLinkText(locatorname);
			default:
				return By.id(locatorname);
		}
	}
	
	public static By getBy(String locatorname) {
		return getBy(ElementType.CSSSELECTOR, locatorname);
	}

	public static void forceToWait(int suspendTimeInSeconds) {
		forceToWaitInMilli(suspendTimeInSeconds * 1000L);
	}
	
	public static void forceToWaitInMilli(long suspendTimeInMilliSeconds) {
		try {
			Thread.sleep(suspendTimeInMilliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static int compareStringNumber(String str1, String str2) {
		return str1.length() < str2.length() ? -1 : str1.compareTo(str2);
	}
	
	public static int extractNumberFromString(String str) {
		Matcher m = numberP.matcher(str);
		if (m.find()) {
			return Integer.parseInt(m.group(0).trim());
		}
		return -1;
	}
	
	public static String getElementTextContent(WebElement webElement){
		return webElement.getAttribute("innerText").replaceAll("\n", "").trim();
	}
	
	public static String getElementTextContentWithSpecialChr(WebElement webElement){
		return webElement.getAttribute("innerText").replaceAll("\n", "").replaceAll("¡¯", "'").trim();
	}
	
	public static void waitForLoadFinished(WebDriver driver) {
		do {
			Tools.forceToWait(1);
		} while (runJSScript(driver, "return document.querySelector('ion-loading');") !=null);
	}
	
	public static Object runJSScript(WebDriver driver, String script) {
		if (driver instanceof JavascriptExecutor) {
			TestLogger.trace((String.format("js script: %s",script)));
			return ((JavascriptExecutor) driver).executeScript(script);
		}
		return null;
	}
	
	public static void setContentToSystemClipboard(String content) {
		StringSelection s = new StringSelection(content);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(s, null);
	}
	
	public static boolean isEmptyString(String str) {
		return str == null || str.trim().length() == 0 ? true : false;
	}
	
	public static String prependZero(int number) {
		return number < 10 ? String.format("0%s", number) : String.valueOf(number);
	}
	
}
