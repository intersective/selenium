package testsuit;


import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pom.pe.AccountPage;
import pom.pe.DashboardPage;
import pom.pe.ForgetPasswordPage;
import pom.pe.LoginPage;
import respositry.LocalStore;
import service.Tools;
import service.UIAction;

import common.BuildConfig;
import common.ElementType;
import common.ShareConfig;


public class TestLogin extends TestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test login");
	}
	
	/**
	 * login test case
	 * check login form and its relevant email and password field exist
	 * login button should not enabled until something entered into the email field
	 */
	@Test(description = "test login", groups = "first")
	public void main() {
		LocalStore ls = LocalStore.getInstance();
		if (ShareConfig.isTestLoginRun) {
			return;
		}
		
		LoginPage loginPage = new LoginPage();
		loginPage.go(driver);
		
		String userName = BuildConfig.userName;
		String upassword = BuildConfig.password;
		ls.addValue("userName", userName);
		
		Assert.assertNotNull(loginPage.getForgetPasswordBtn(sw));
		ForgetPasswordPage forgetPasswordPage = new ForgetPasswordPage();
		forgetPasswordPage.go(sw);
		Assert.assertNotNull(forgetPasswordPage.getForgetForm(sw));
		Tools.forceToWait(BuildConfig.jsWaitTime);
		WebElement contactEmail = forgetPasswordPage.getContactEmail(sw);
		contactEmail.sendKeys(new String[] { userName });
		Assert.assertNotNull(forgetPasswordPage.getSendEmailBtn(sw));
		WebElement sucessMessage;
		do {
			sucessMessage = sw.waitForElement("ion-toast", ElementType.TAGNAME, BuildConfig.consecutiveCheckTime);
		} while (sucessMessage != null);
		forgetPasswordPage.back(sw);
		
		Tools.forceToWait(BuildConfig.jsWaitTime);
		loginPage.login(sw, userName, userName);// a test of using wrong password
		Tools.forceToWait(BuildConfig.jsWaitTime);
		Assert.assertNotNull(sw.waitForElement(".alert-wrapper > .alert-message"));
		UIAction.waitForElementVisibleWithAssert(sw, ".alert-wrapper > .alert-button-group > button").click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		WebElement passwordField = loginPage.getPasswordField(sw);
		passwordField.clear();
		passwordField.sendKeys(new String[] { upassword });
		loginPage.getLoginBtn(sw).click();
		
		sw.waitForDocumentReady(BuildConfig.pageWaitTime);
		AccountPage accountPage = new AccountPage();
		WebElement skipBtn;
		if (accountPage.getTutorialSlides(sw) != null && (skipBtn = accountPage.getSkipBtn(sw))!= null) {
			if (skipBtn.isEnabled()) {
				skipBtn.click();
			}
		}
		accountPage.getVideo(sw);
		WebElement doneBtn = accountPage.getVideoDoneBtn(sw);
		if (doneBtn != null) {
			Tools.forceToWait(5);
			doneBtn.click();
		}
		waitForLoadFinished();
		
		waitForLoadFinished();
		DashboardPage dashboardPage = new DashboardPage();
		Assert.assertNotNull(dashboardPage.getTab(sw));
		ls.addUserPoints(dashboardPage.getUserPoints(sw));
		WebElement t3Badge = sw.waitForElement("#tab-t0-3 ion-badge");
		if (t3Badge != null) {
			ls.addSpinChances(Tools.getElementTextContent(t3Badge));
		}
	}

}
