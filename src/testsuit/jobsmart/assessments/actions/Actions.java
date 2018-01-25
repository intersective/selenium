package testsuit.jobsmart.assessments.actions;


import org.openqa.selenium.WebElement;

import service.PageAction;
import service.SeleniumWaiter;
import service.Tools;
import service.UIAction;


public class Actions implements PageAction {

	public void login(SeleniumWaiter sw, String userName, String password) {
		WebElement loginForm = UIAction.waitForElementVisible(sw, "form[name='jobsmart']");
		loginForm.findElement(Tools.getBy("input[name=uEmail]")).clear();
		loginForm.findElement(Tools.getBy("input[name=uEmail]")).sendKeys(new String [] { userName });
		loginForm.findElement(Tools.getBy("input[name=password]")).clear();
		loginForm.findElement(Tools.getBy("input[name=password]")).sendKeys(new String [] { password });
		loginForm.findElement(Tools.getBy("input[type='submit']")).click();
	}

	public void logout(SeleniumWaiter sw) {
		sw.waitForElement(".tab-nav > a:nth-of-type(4)").click();
		
		sw.waitForElement(".pane[nav-view='active'] ion-item[ng-click='logout()']").click();
	}
	
}
