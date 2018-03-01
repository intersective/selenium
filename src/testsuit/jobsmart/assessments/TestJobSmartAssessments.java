package testsuit.jobsmart.assessments;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Question;
import model.jobsmart.JobSmartAssessment;
import model.jobsmart.MileStone;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.AssignmentDataService;
import service.TestLogger;
import service.Tools;
import testsuit.JobSmartTestTemplate;

import common.ElementType;


public class TestJobSmartAssessments extends JobSmartTestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test job smart assessments");
	}
	
	@Test(description = "test questions for Job smart phase 1", groups = "practera_jobsmart_assessment")
	public void main() {
		waitForLoadFinished();
		sw.waitForElement("//div[@nav-bar='active']/ion-header-bar/div[text()='My Score']", ElementType.XPATH);
		Tools.forceToWait(3);
		int currentPoints;
		try {
			currentPoints = Integer.parseInt(sw.waitForElement("#fillgaugeScore > g > g > text").getAttribute("textContent").trim());
		} catch (Exception e) {
			currentPoints = 0;
		}
		sw.waitForElement(".tab-nav > a:nth-of-type(2)").click();
		waitForLoadFinished();
		MileStone mileStone = null;
		try {
			mileStone = AssignmentDataService.getInstance().loadDataFromJsonFile(
					String.format("%s%sdata%sjobsmart%sphase1%s%s",
							System.getProperty("user.dir"), File.separator, File.separator, File.separator, File.separator, "workflow-1.json"), MileStone.class);
		} catch (IOException e) {
			TestLogger.error(e.getMessage());
		}
		if (mileStone == null) {
			return;
		}
		
		List<WebElement> activities = sw.waitForListContent(".activities > div > div[parent='app']");
		int total = activities.size() + 2;
		ArrayList<JobSmartAssessment> assessments = mileStone.getActvitySequences().get(0).getAssessments();
		for (int i = 2; i < total; i++) {
			WebElement oneAct = sw.waitForElement(String.format(".activities > div > div[parent='app']:nth-of-type(%s)", i));
			String activityName = Tools.getElementTextContent(findElement(oneAct, ".card-title"));
			if (!oneAct.isDisplayed()) {
				scrollToElement(oneAct);
				Tools.forceToWait(1);
			}
			
			if (filterActivity(oneAct, i)) {
				continue;
			}
			
			JobSmartAssessment assessment = null;
			for (JobSmartAssessment j : assessments) {
				if (j.getName().equals(activityName)) {
					assessment = j;
				}
			}
			if (assessment == null) {
				TestLogger.trace(String.format("can not find [%s] [%s] assessment", i, activityName));
				continue;
			}
			
			List<WebElement> buttons = findElements(oneAct, "button");
			scrollIfNotVisible(buttons.get(buttons.size() - 1)).click();
			waitForLoadFinished();
			if (sw.waitForElement("ion-view[nav-view='active'][state='app.assessment']") == null) {
				TestLogger.trace(String.format("[%s] [%s] assessment is not an assessment", i, activityName));
				back();
				continue;
			}
			
			doAssessment(assessment);
			int points = clickAfterFinishAssessment();
			TestLogger.trace(String.format("gained point [%s]", points));
			int incrementedPoints = Integer.parseInt(sw.waitForElement("#fillgaugeScore > g > g > text").getAttribute("textContent").trim());
			try {
				Assert.assertEquals(incrementedPoints - currentPoints, points);
			} catch (AssertionError error) {
				TestLogger.trace(error.getMessage());
			}
			currentPoints = incrementedPoints;
			sw.waitForElement(".tab-nav > a:nth-of-type(2)").click();
			waitForLoadFinished();
			back();// after finished an activity, the page will stay at the activity detail, so we need to go back
			Tools.forceToWait(2);
		}
	}
	
	protected boolean filterActivity(WebElement oneAct, int i) {
		if (findElement(oneAct, "i.tick") != null) {
			TestLogger.trace(String.format("activity [%s] done", i));
			return true;
		}
		List<WebElement> buttons = findElements(oneAct, "button");
		if (buttons == null || buttons.size() == 0) {
			TestLogger.trace(String.format("activity [%s] not avaible for doing", i));
			return true;
		}
		if (buttons.get(buttons.size() - 1).getAttribute("class").indexOf("active") > 0) {
			TestLogger.trace(String.format("activity [%s] coming soon", i));
			return true;
		}
		if (Tools.getElementTextContent(buttons.get(buttons.size() - 1)).equals("Reviewing")) {
			TestLogger.trace(String.format("activity [%s] is Reviewing", i));
			return true;
		}
		
		if (Tools.getElementTextContent(buttons.get(buttons.size() - 1)).equals("Feedback")) {
			TestLogger.trace(String.format("we gave a Feedback for activity [%s]", i));
			return true;
		}
		return false;
	}
	
	protected void doAssessment(JobSmartAssessment assessment) {
		List<WebElement> questions = sw.waitForListContent("div[ng-repeat='question in group.questions']");
		ArrayList<Question> questionsData = assessment.getQuestions();
		for (WebElement q : questions) {
			boolean found = false;
			String questionTitle = Tools.getElementTextContent(findElement(q, "div > .item"));
			if (!q.isDisplayed()) {
				scrollToElement(q);
			}
			String titleWithoutQuestionIndex = questionTitle.split("\\.", 2)[1].trim();
			for (Question qd : questionsData) {// question might not be in the same order as the page
				if (qd.getQcontent().equals(titleWithoutQuestionIndex)) {
					found = true;
					doQuestion(q.findElement(Tools.getBy("div")), qd.getAnswer());
					Tools.forceToWait(1);
					break;
				}
			}
			if (!found) {
				TestLogger.trace(String.format("we can not find this [%s] assessment in the data file", questionTitle));
			}
		}
	}
	
	/**
	 * perform the click for those modal after finishing an assessment
	 * @return points
	 */
	protected int clickAfterFinishAssessment() {
		waitForVisibleWithScroll("//button[text()='Submit']", ElementType.XPATH).click();
		WebElement submitConfirm = sw.waitForElement(".popup");
		submitConfirm.findElement(Tools.getBy(".popup-buttons > button:nth-of-type(2)")).click();
		Tools.forceToWait(1);
		sw.waitForElement(".popup> .popup-buttons > button").click();
		Tools.forceToWait(2);
		WebElement gotIt = sw.waitForElement(".modal button");
		WebElement numberAnimation = sw.waitForElement(".modal .number-animation");
		int points = 0;
		if (numberAnimation != null) {
			String number = Tools.getElementTextContent(numberAnimation);
			points = Tools.isEmptyString(number) ? 0 : Integer.parseInt(number.substring(1).split(" ")[0].trim());// no points set for this activity
		}
		gotIt.click();
		Tools.forceToWait(3);
		return points;
	}
	
	protected void back() {
		WebElement backBtn = sw.waitForElement("div[nav-bar='active'] .primary-buttons .back-button");
		if (backBtn == null) {
			backBtn = sw.waitForElement("div[nav-bar='active'] .back-button");
		}
		backBtn.click();
		waitForLoadFinished();
	}

	protected void doQuestion(WebElement q, String answer) {
		String type = q.getAttribute("ng-if");
		if (type.contains("text")) {
			scrollIfNotVisible(q.findElement(Tools.getBy("textarea"))).sendKeys(new String[] { answer });
		} else if (type.contains("oneof")) {
			scrollIfNotVisible(q.findElement(Tools.getBy(String.format(".list > label:nth-of-type(%s)", answer)))).click();
		} else if (type.contains("multiple")) {
			String[] chocies = answer.split(",");
			for (String c : chocies) {
				scrollIfNotVisible(q.findElement(Tools.getBy(String.format(".list > label:nth-of-type(%s)", c)))).click();
			}
		} else if (type.contains("file")) {
			String subType = q.findElement(Tools.getBy("div:nth-of-type(1)")).getAttribute("ng-if");
			if (subType.contains("any") || subType.contains("video") || subType.contains("image")) {
				scrollIfNotVisible(q.findElement(Tools.getBy("div:nth-of-type(1) button:nth-of-type(1)"))).click();
				Tools.forceToWait(5);
				fileUpload(answer);
			}
		} else if (type.contains("video")) {
			scrollIfNotVisible(q.findElement(Tools.getBy("button:nth-of-type(1)"))).click();
			Tools.forceToWait(5);
			postVideoByUrl(answer);
		}
	}
	
}
