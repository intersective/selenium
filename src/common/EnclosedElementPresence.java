package common;


import java.util.function.Function;

import org.openqa.selenium.WebElement;

import service.Tools;


public class EnclosedElementPresence implements Function<WebElement, WebElement> {

	private String mLocator;
	private ElementType mFlag;
	
	public EnclosedElementPresence(String locator, ElementType flag) {
		super();
		mLocator = locator;
		mFlag = flag;
	}

	@Override
	public WebElement apply(WebElement we) {
		return we.findElement(Tools.getBy(mFlag,mLocator));
	}
	
}
