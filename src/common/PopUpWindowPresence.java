package common;


import java.util.function.Function;

import org.openqa.selenium.WebDriver;


public class PopUpWindowPresence implements Function<WebDriver, Boolean> {

	public PopUpWindowPresence() {
		super();
	}

	@Override
	public Boolean apply(WebDriver t) {
		return (t.getWindowHandles().size() != 1);
	}

}
