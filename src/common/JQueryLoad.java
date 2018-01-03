package common;


import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;


public class JQueryLoad implements ExpectedCondition<Boolean> {

	@Override
	public Boolean apply(WebDriver arg0) {
		try {
			Long r = (Long)((JavascriptExecutor)arg0).executeScript("return $.active");
			return r == 0;
		} catch (Exception e) {
			return true;
		}
	}

}
