package testsuit.events.actions;


import org.openqa.selenium.WebDriver;

import service.SeleniumWaiter;


public class MobileActions extends Actions {

	@Override
	public String selectAnEvent(WebDriver driver, SeleniumWaiter sw, int eventPosition) {
		return super.selectAnEvent(driver, sw, eventPosition);
	}

	@Override
	public int getEventPosition(SeleniumWaiter sw, String eventName) {
		return super.getEventPosition(sw, eventName);
	}

	@Override
	public String getEventIdentifier(WebDriver driver, int eventPosition) {
		return super.getEventIdentifier(driver, eventPosition);
	}

}
