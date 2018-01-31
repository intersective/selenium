package testsuit.jobsmart.assessments.phase3;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.MentorAnswer;
import model.MentorQuestion;
import model.Workflow;
import model.jobsmart.JobSmartAssessment;
import model.jobsmart.MileStone;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.AssignmentDataService;
import service.PageActionFactory;
import service.TestLogger;
import service.Tools;
import testsuit.jobsmart.assessments.TestJobSmartAssessments;
import testsuit.jobsmart.assessments.actions.Actions;

import common.BuildConfig;
import common.ElementType;


public class TestJobSmartPhase3Assessments extends TestJobSmartAssessments {

	private Actions actions;
	private testsuit.practera.actions.Actions practeraActions;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test job smart assessments for phase 3");
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.jobsmart.assessments.actions.Actions");
		practeraActions = (testsuit.practera.actions.Actions) PageActionFactory.getInstance().build("testsuit.practera.actions.Actions");
	}
	
	private WebElement retrieveAssessmentElement(JobSmartAssessment assessment) {
		List<WebElement> assessmentsElements = sw.waitForListContent(".content-container > div#assessments > .tab-content > #moderated > div> table > tbody > tr");
		int atotal = assessmentsElements.size() + 1;
		WebElement assessmentElement = null;
		for (int i = 1; i < atotal; i++) {
			assessmentElement = sw.waitForElement(String.format(".content-container > div#assessments > .tab-content > #moderated > div > table > tbody > tr:nth-of-type(%s)", i));
			String displayAssessmentName = Tools.getElementTextContent(findElement(assessmentElement, "td:nth-of-type(1) > a"));
			if (assessment.getPracteraName().equals(displayAssessmentName)) {
				break;
			}
		}
		return assessmentElement;
	}
	
	private void doQuestionWithAssessment(JobSmartAssessment assessment) {
		List<WebElement> activities = sw.waitForListContent(".activities > div > div[parent='app']");
		int total = activities.size() + 2;
		boolean found = false;
		for (int i = 2; i < total && !found; i++) {
			WebElement oneAct = sw.waitForElement(String.format(".activities > div > div[parent='app']:nth-of-type(%s)", i));
			if (!oneAct.isDisplayed()) {
				scrollToElement(oneAct);
				Tools.forceToWait(1);
			}
			
			if (filterActivity(oneAct, i)) {
				continue;
			}
			
			String activityName = Tools.getElementTextContent(findElement(oneAct, ".card-title"));
			if (!assessment.getName().equals(activityName)) {
				TestLogger.trace(String.format("we would not work on this - %s - this time", activityName));
				continue;
			}
			
			List<WebElement> buttons = findElements(oneAct, "button");
			scrollIfNotVisible(buttons.get(buttons.size() - 1)).click();
			waitForLoadFinished();
			if (sw.waitForElement("ion-view[nav-view='active'][state='app.assessment']") == null) {
				TestLogger.trace(String.format("%s assessment is not an assessment", i));
				back();
				continue;
			}
			found = true;
		}
		if (!found) {
			TestLogger.trace(String.format("we can not find (%s)", assessment.getName()));
			return;
		}
		
		doAssessment(assessment);
		clickAfterFinishAssessment();
		sw.waitForElement(".tab-nav > a:nth-of-type(2)").click();
		waitForLoadFinished();
		back();// after finished an activity, the page will stay at the activity detail, so we go back to the activity list page
		Tools.forceToWait(2);
	}
	
	private void assignSubmissionToReviewer(JobSmartAssessment assessment, String studentName) {
		WebElement assessmentElement = retrieveAssessmentElement(assessment);
		Assert.assertNotNull(assessmentElement);
		
		assessmentElement.findElement(Tools.getBy("td:nth-of-type(3) > a")).click();// unassigned
		sw.waitForElement("#reviewContainer > div#assessments > ul#reviewTab > li:nth-of-type(2)");
		Tools.forceToWait(1);
		
		WebElement popover = null;
		List<WebElement> unassigned = sw.waitForListContent("#reviewContainer > div#assessments > div > div#unassigned > div > table > tbody > tr");
		if (unassigned == null) {
			unassigned = new ArrayList<WebElement>();
		}
		int index = 0;
		for (WebElement one: unassigned) {
			if (studentName.equals(Tools.getElementTextContent(one.findElement(Tools.getBy("td:nth-of-type(1) > span"))))) {
				one.findElement(Tools.getBy("td:nth-of-type(3) > span a")).click();
				popover = sw.waitForElement(one, "td:nth-of-type(3) > span > div.popover", ElementType.CSSSELECTOR, BuildConfig.jsWaitTime);
				break;
			}
			index++;
		}
		if (popover != null) {// would be null if we run the scripts again
			popover.findElement(Tools.getBy(".popover-content input")).sendKeys(new String[] { BuildConfig.jobsmartMentor.split("@")[0] });
			sw.waitForElement("ul.select2-results > li > div").click();
			popover.findElement(Tools.getBy(".popover-content button.editable-submit")).click();
		}
		
		do {
			popover = sw.waitForElement(String.format("#reviewContainer > div#assessments > div > div#unassigned > div > table > tbody > tr:nth-of-type(%s) td:nth-of-type(3) > span > div.popover", ++index),
					ElementType.CSSSELECTOR, BuildConfig.consecutiveCheckTime);
		} while (popover != null);
		practeraActions.waitToastMessageDisappear(sw);
	}
	
	private void review(JobSmartAssessment assessment) {
		scrollIfNotVisible(sw.waitForElement("div#start-page > div.form-actions > button")).click();
		
		List<MentorQuestion> mentorQuestions = assessment.getMentorQuestions();
		List<WebElement> displayMentorQuestions = sw.waitForListContent(".question");
		int total = displayMentorQuestions.size() + 1;
		int dataIndex = 0;
		for (int i = 1; i < total; i++) {
			WebElement reviewQuestion = sw.waitForElement(String.format(".question:nth-of-type(%s)", i));
			scrollIfNotVisible(reviewQuestion);
			MentorQuestion mq = mentorQuestions.get(dataIndex);
			if (mq.getName().equals(Tools.getElementTextContent(findElement(reviewQuestion, "#question-name")))) {
				dataIndex++;
				List<MentorAnswer> mentorAnswers = mq.getMentorAnswers();
				for (MentorAnswer ma : mentorAnswers) {
					if (ma.getType().equals("oneof")) {
						scrollIfNotVisible(findElement(reviewQuestion, String.format(".radio:nth-of-type(%s) span", ma.getAnswer()))).click();
					} else if (ma.getType().equals("text")) {
						scrollIfNotVisible(findElement(reviewQuestion, "#choice-comment textarea")).sendKeys(new String[] { ma.getAnswer() });
					} else if (ma.getType().equals("free text review")) {
						List<WebElement> choiceText = findElements(reviewQuestion, "#choice-text");
						scrollIfNotVisible(findElement(choiceText.get(choiceText.size() - 1), "textarea")).sendKeys(new String[] { ma.getAnswer() });
					}
				}
			}
		}
		scrollIfNotVisible(findElement("button#submit")).click();
		practeraActions.waitToastMessageDisappear(sw);
	}
	
	@Test(description = "test questions for Job smart phase 3", groups = "practera_jobsmartp3_assessment")
	public void main() {
		ArrayList<MileStone> workflows = new ArrayList<MileStone>();
		try {
			Workflow workflow = AssignmentDataService.getInstance().loadDataFromJsonFile(String.format("%s%sdata%sjobsmart%sphase3%s%s.json",
					System.getProperty("user.dir"), File.separator, File.separator, File.separator, File.separator, "item-1"), Workflow.class);
			ArrayList<String> sequences = workflow.getSequences();
			for (String s : sequences) {
				workflows.add(AssignmentDataService.getInstance().loadDataFromJsonFile(
						String.format("%s%sdata%sjobsmart%sphase3%s%s",
								System.getProperty("user.dir"), File.separator, File.separator, File.separator, File.separator, s), MileStone.class));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		for (MileStone mileStone : workflows) {
			driver.get(BuildConfig.jobsmartUrl);	
			Tools.forceToWait(10);
			actions.login(sw, BuildConfig.jobsmartStudent, BuildConfig.jobsmartStudentPassword);
			
			waitForLoadFinished();
			sw.waitForElement("//div[@nav-bar='active']/ion-header-bar/div[text()='My Score']", ElementType.XPATH);
			Tools.forceToWait(3);
			sw.waitForElement(".tab-nav > a:nth-of-type(2)").click();
			waitForLoadFinished();
			
			JobSmartAssessment assessment = mileStone.getActvitySequences().get(0).getAssessments().get(0);// only one assessment defined in a actvitySequence of each workflow
			doQuestionWithAssessment(assessment);
			actions.logout(sw);
			
			if (assessment.isPublishScore()) {// only if there is a moderated assessment which requires reviewing
				driver.get(BuildConfig.practeraUrl);
				practeraActions.login(sw, BuildConfig.jobsmartAdmin, BuildConfig.jobsmartAdminPassword);
				WebElement sideBar = practeraActions.getSidebar(sw);
				WebElement project = sideBar.findElement(Tools.getBy("ul.nav li:nth-of-type(2)"));
				project.findElement(Tools.getBy(ElementType.TAGNAME, "a")).click();
				Tools.forceToWait(BuildConfig.jsWaitTime);
				project.findElement(Tools.getBy("ul.submenu li:nth-of-type(3)")).click();
				
				String studentName = BuildConfig.jobsmartStudent.split("@")[0];
				assignSubmissionToReviewer(assessment, studentName);
				
				sw.waitForElement("#reviewContainer > div#assessments > ul#reviewTab > li:nth-of-type(3) > a").click();// ready to review
				List<WebElement> reviewinprogress = sw.waitForListContent("#assessments #reviewinprogress table > tbody > tr");// ready to review list
				if (reviewinprogress != null && reviewinprogress.size() > 0) {
					int reviewTotal = reviewinprogress.size() + 1;
					for (int i = 1; i < reviewTotal; i++) {
						WebElement one = sw.waitForElement(String.format("#assessments #reviewinprogress table > tbody > tr:nth-of-type(%s)", i));
						if (studentName.equals(Tools.getElementTextContent(findElement(one,"td > span")))){
							findElement(one, "td:nth-of-type(3) > span > a:nth-of-type(2)").click();
							review(assessment);
							break;
						}
					}
				}
				
				sideBar = practeraActions.getSidebar(sw);// since we will go to the assessment centre page, we must use the side bar to get to the assessment page
				project = sideBar.findElement(Tools.getBy("ul.nav li:nth-of-type(2)"));
				project.findElement(Tools.getBy(ElementType.TAGNAME, "a")).click();
				Tools.forceToWait(BuildConfig.jsWaitTime);
				project.findElement(Tools.getBy("ul.submenu li:nth-of-type(3)")).click();
				
				WebElement assessmentElement = retrieveAssessmentElement(assessment);
				assessmentElement.findElement(Tools.getBy("td:nth-of-type(5) > a")).click();// ready to publish
				sw.waitForElement("#reviewContainer > div#assessments > ul#reviewTab > li:nth-of-type(4)");
				List<WebElement> readyToPublish = sw.waitForListContent("#reviewContainer > div#assessments > div > div#readytopublish > div > table > tbody > tr");
				if (readyToPublish != null && readyToPublish.size() > 0) {
					for (WebElement one: readyToPublish) {
						if (studentName.equals(Tools.getElementTextContent(one.findElement(Tools.getBy("td:nth-of-type(1) > span"))))) {
							Tools.disableConfirmWindow(driver);
							Tools.forceToWait(BuildConfig.jsWaitTime);
							one.findElement(Tools.getBy("td:nth-of-type(5) > span:nth-of-type(2) > a:nth-of-type(1)")).click();
							practeraActions.waitToastMessageDisappear(sw);
							break;
						}
					}
				}
				practeraActions.logout(sw, "4");
			}
		}
	}
		
}
