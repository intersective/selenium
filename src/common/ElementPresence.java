package common;


import java.util.function.Function;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import service.Tools;


public class ElementPresence implements Function<WebDriver, WebElement> {

	private String mLocator;
	private ElementType mFlag;
	
	public ElementPresence(String locator, ElementType flag) {
		super();
		mLocator = locator;
		mFlag = flag;
	}

	@Override
	public WebElement apply(WebDriver t) {
		return t.findElement(Tools.getBy(mFlag,mLocator));
	}
	
}
