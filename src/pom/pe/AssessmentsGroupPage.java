package pom.pe;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import common.ElementType;
import pom.Page;
import service.SeleniumWaiter;
import service.Tools;


public class AssessmentsGroupPage implements Page {

	@Override
	public void go(WebDriver driver) {
	}

	@Override
	public void go(SeleniumWaiter sw) {
	}

	@Override
	public void back(SeleniumWaiter sw) {
		sw.waitForElement("assessments-group-page ion-header ion-navbar.toolbar button.back-button").click();
	}

	public WebElement getGroupPage(SeleniumWaiter sw) {
		return sw.waitForElement("assessments-group-page");
	}
	
	public WebElement getGroupsQuestion(WebElement assessmentsGroupPage) {
		return assessmentsGroupPage.findElement(Tools.getBy(".groups-question"));
	}
	
	public List<WebElement> getQuestions(WebElement groupsQuestion) {
		return groupsQuestion.findElements(Tools.getBy(".assessments-group-text"));
	}
	
	public WebElement getAssessmentsGroupDescription(WebElement question) {
		return question.findElement(Tools.getBy(ElementType.TAGNAME, "div"));
	}
	
	public WebElement getQuestion(WebElement groupsQuestion, int index) {
		return groupsQuestion.findElement(Tools.getBy(String.format("div.assessments-group-text:nth-of-type(%s)" , index)));
	}
	
	public WebElement getQuestionDescription(WebElement question) {
		return question.findElement(Tools.getBy("ion-list span"));
	}
	
	public void save(WebElement assessmentsGroupPage) {
		assessmentsGroupPage.findElement(Tools.getBy("ion-navbar.toolbar ion-buttons button.btn-save")).click();
	}
	
}
