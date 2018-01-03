package pom.pe;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pom.Page;
import service.SeleniumWaiter;
import service.Tools;
import service.UIAction;


public class AssessmentsPage implements Page {

	@Override
	public void go(WebDriver driver) {
	}

	@Override
	public void go(SeleniumWaiter sw) {
	}

	@Override
	public void back(SeleniumWaiter sw) {
		sw.waitForElement("assessments-page ion-header ion-navbar.toolbar button.back-button").click();
	}

	public WebElement getAssessmentHeader(SeleniumWaiter sw) {
		return sw.waitForElement("assessments-page ion-content ion-card-content.card-content");
	}

	public WebElement getAssessmentTitle(SeleniumWaiter sw) {
		return sw.waitForElement("assessments-page ion-content ion-card-content.card-content .assessment-title");
	}
	
	public WebElement getAssessmentDescription(SeleniumWaiter sw) {
		return sw.waitForElement("assessments-page ion-content ion-card-content.card-content .assessment-description");
	}
	
	public List<WebElement> getQuestionGroups(SeleniumWaiter sw) {
		return sw.waitForElements("assessments-page ion-content questiongroup");
	}
	
	public WebElement getQuestionGroupTitle(WebElement questionGroup) {
		return questionGroup.findElement(Tools.getBy(".questionGroup .label"));
	}
	
	public WebElement getQuestionGroupDescription(WebElement questionGroup) {
		return questionGroup.findElement(Tools.getBy(".questionGroup .description"));
	}
	
	public WebElement getQuestionGroup(SeleniumWaiter sw, int index) {
		return UIAction.waitForElementVisible(sw, String.format("assessments-page ion-content questiongroup:nth-of-type(%s) > .questionGroup", index));
	}
	
	public WebElement getSubmitBtn(SeleniumWaiter sw) {
		return sw.waitForElement("assessments-page ion-header ion-navbar.toolbar button.btn-submit");
	}
	
}
