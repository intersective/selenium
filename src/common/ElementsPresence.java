package common;


import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import service.Tools;


public class ElementsPresence implements Function<WebDriver, List<WebElement>> {

	private String mLocator;
	private ElementType mFlag;
	
	public ElementsPresence(String locator, ElementType flag) {
		super();
		mLocator = locator;
		mFlag = flag;
	}

	@Override
	public List<WebElement> apply(WebDriver t) {
		return t.findElements(Tools.getBy(mFlag,mLocator));
	}
	
}
