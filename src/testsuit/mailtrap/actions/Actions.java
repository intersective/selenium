package testsuit.mailtrap.actions;


import service.PageAction;
import service.SeleniumWaiter;
import service.Tools;

import common.BuildConfig;
import common.ElementType;


public class Actions implements PageAction {

	public void login(SeleniumWaiter sw, String userName, String password) {
		sw.waitForElement(".signin_block > a:nth-of-type(1)").click();
		sw.waitForElement("#new_user #user_email").sendKeys(new String[] { BuildConfig.mailtrapUser });
		sw.waitForElement("#new_user #user_password").sendKeys(new String[] { BuildConfig.mailtrapPassword });
		sw.waitForElement("#new_user input[type=submit]").click();
		
		sw.waitForElement("//*[@class='initial']/strong/a/span[text()='practera']/..", ElementType.XPATH).click();
		Tools.forceToWait(2);
	}
	
	public void logout(SeleniumWaiter sw) {
		sw.waitForElement(".account-name").click();// bring out the account drop-down menu
		Tools.forceToWait(BuildConfig.jsWaitTime);
		sw.waitForElement(".account-dropdown > li:nth-of-type(4)").click();// logout button
	}
	
}
