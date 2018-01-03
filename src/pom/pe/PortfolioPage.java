package pom.pe;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pom.Page;
import service.SeleniumWaiter;
import service.Tools;


public class PortfolioPage implements Page {

	@Override
	public void go(WebDriver driver) {
	}

	@Override
	public void go(SeleniumWaiter sw) {
	}

	@Override
	public void back(SeleniumWaiter sw) {
	}

	public List<WebElement> getContents(SeleniumWaiter sw) {
		return sw.waitForListContent(".container > .row > div");
	}
	
	public List<WebElement> getHeaders(List<WebElement> contents) {
		return contents.get(0).findElements(Tools.getBy(".text-center"));
	}
	
}
