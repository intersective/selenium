package common;


import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.WebElement;

import service.Tools;


public class EnclosedElementsPresence implements Function<WebElement, List<WebElement>> {

	private String mLocator;
	private ElementType mFlag;
	
	public EnclosedElementsPresence(String locator, ElementType flag) {
		super();
		mLocator = locator;
		mFlag = flag;
	}

	@Override
	public List<WebElement> apply(WebElement we) {
		return we.findElements(Tools.getBy(mFlag,mLocator));
	}
	
}
