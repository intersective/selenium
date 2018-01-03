package testsuit.practera.actions;


import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import service.SeleniumWaiter;
import service.TestLogger;
import service.UIAction;


public class MobileActions extends Actions {

	@Override
	public void logout(SeleniumWaiter sw, String userMenuLocation) {
		UIAction.waitForElementVisible(sw, "#navbar-container > button.navbar-toggle:nth-of-type(2)").click();
		super.logout(sw, userMenuLocation);
	}

	@Override
	public void login(SeleniumWaiter sw, String userName, String password) {
		WebElement hamburger = sw.waitForElement("button[type=button].navbar-toggle");
		if (hamburger != null && hamburger.isDisplayed()) {
			hamburger.click();
		}
		super.login(sw, userName, password);
	}

	@Override
	public WebElement getSidebar(SeleniumWaiter sw) {
		WebElement menuToggler = UIAction.waitForElementVisible(sw, "#navbar-container > button#menu-toggler");
		if (menuToggler != null) {
			menuToggler.click();
		}
		return super.getSidebar(sw);
	}

	@Override
	public void enterToEnrollmentPage(SeleniumWaiter sw, WebDriver driver) {
		WebElement firstRow = sw.waitForElement(".content-container .row:nth-of-type(1)");
		Assert.assertNotNull(firstRow);
		String script = "document.querySelector('.content-container .row:nth-of-type(1)  a:nth-of-type(4)').click();";// the button is covered by a div block
		if (driver instanceof JavascriptExecutor) {
			TestLogger.trace((String.format("js script: %s",script)));
			((JavascriptExecutor) driver).executeScript(script);
		}
	}

	@Override
	public void waitToastMessageDisappear(SeleniumWaiter sw) {
		super.waitToastMessageDisappear(sw);
	}

	@Override
	public int waitForReviewContent(SeleniumWaiter sw) {
		return super.waitForReviewContent(sw);
	}

	@Override
	public void getResultFromDropBoxList(SeleniumWaiter sw, String searchItem) {
		super.getResultFromDropBoxList(sw, searchItem);
	}
	
}
