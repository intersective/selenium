package testsuit;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pom.pe.AccountPage;
import service.Tools;

import common.BuildConfig;


public class TestLogout extends TestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test log out");
	}

	@Test(description = "test log out")
	public void main() {
		Tools.forceToWait(BuildConfig.jsWaitTime);
		AccountPage accountPage = new AccountPage();
		accountPage.go(sw);
		accountPage.logout(driver, sw);
	}

}
