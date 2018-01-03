package pom.pe;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pom.Page;
import service.SeleniumWaiter;


public class ActivityDetailPage implements Page {

	@Override
	public void go(WebDriver driver) {
	}

	@Override
	public void go(SeleniumWaiter sw) {
	}

	@Override
	public void back(SeleniumWaiter sw) {
		sw.waitForElement("ion-header.activity-detail ion-navbar.toolbar button.back-button").click();
	}

	public List<WebElement> getSubmissions(SeleniumWaiter sw) {
		return sw.waitForListContent("ion-content.activity-detail > div > .submissions > .submissions-information");
	}
	
	public WebElement getAddSubmissionBtn(SeleniumWaiter sw) {
		return sw.waitForElement("ion-content.activity-detail button.add-submission");
	}
	
}
