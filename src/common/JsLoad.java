package common;


import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;


public class JsLoad implements ExpectedCondition<Boolean> {

	@Override
	public Boolean apply(WebDriver arg0) {
		return ((JavascriptExecutor)arg0).executeScript("return document.readyState").toString().equals("complete");
	}

}
