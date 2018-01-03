package pom;


import org.openqa.selenium.WebDriver;

import service.SeleniumWaiter;


public interface Page {

	/**
	 * Abstract method for going to the page
	 * @param driver
	 */
	public void go(WebDriver driver);

	/**
	 * Abstract method for going to the page
	 * @param sw
	 */
	public void go(SeleniumWaiter sw);
	
	/**
	 * Abstract method for back to previous page action
	 * @param sw
	 */
	public void back(SeleniumWaiter sw);
	
}
