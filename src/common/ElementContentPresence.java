package common;


import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import service.Tools;


public class ElementContentPresence implements Function<WebDriver, Boolean> {

	private String mLocator;
	private ElementType mFlag;
	
	public ElementContentPresence(String locator, ElementType flag) {
		super();
		mLocator = locator;
		mFlag = flag;
	}

	@Override
	public Boolean apply(WebDriver wd) {
		List<WebElement> t = wd.findElements(Tools.getBy(mFlag, mLocator));
		return t != null && t.size() > 0;
	}
	
}
