package testsuit.dashboard;


import java.util.Arrays;
import java.util.List;

import model.Question;
import model.Questionnaire;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import pom.pe.ActivityDetailPage;
import pom.pe.AssessmentsGroupPage;
import pom.pe.AssessmentsPage;
import pom.pe.DashboardPage;
import service.TestLogger;
import service.Tools;
import testsuit.TestTemplate;
import testsuit.dashboard.action.Actions;

import com.google.common.base.Throwables;
import common.BuildConfig;


public abstract class AssignmentTestTemplate extends TestTemplate {
	
	protected Actions dashboardActions;
	protected DashboardPage dashboardPage;
	protected ActivityDetailPage activityDetailPage;
	protected AssessmentsPage assessmentsPage;
	protected AssessmentsGroupPage assessmentsGroupPage;
	
	@BeforeClass
	public void setup() {
		super.setup();
		dashboardPage = new DashboardPage();
		dashboardActions = dashboardPage.getActions();
	}

	private void completeQuestion(Question question, WebElement element, int index, boolean failed) {
		TestLogger.trace(String.format("completing %s question %s", question.getQtype(), index));
		String answer = failed ? question.getFailAnswer() : question.getAnswer();
		switch (question.getQtype()) {
			case "oneof-question":
				WebElement one = element.findElement(Tools.getBy(String.format("oneof-question ion-list ion-item:nth-of-type(%s) ion-radio button",
						answer)));
				one = scrollIfNotVisible(one);
				one.click();
				break;
			case "text-question":
				WebElement two = element.findElement(Tools.getBy("text-question ion-textarea.input textarea"));
				two = scrollIfNotVisible(two);
				two.sendKeys(new String[] { answer });
				break;
			default:
				break;
		}
		TestLogger.trace(String.format("completed %s question %s", question.getQtype(), index));
	}
	
	protected void checkAnswer(String questionType, WebElement element, String fillContent) {
		switch (questionType) {
			case "oneof-question":
				Assert.assertTrue((element.findElement(Tools.getBy(String.format("oneof-question ion-list ion-item:nth-of-type(%s) ion-radio div",
						fillContent))).getAttribute("class").contains("radio-checked")));
				break;
			case "text-question":
				Assert.assertNotNull((element.findElement(Tools.getBy("text-question ion-textarea.input textarea")).getAttribute("value")));
				break;
			default:
				break;
		}
	}
	
	protected void back(WebElement element) {
		element.findElement(Tools.getBy("ion-navbar.toolbar button.back-button")).click();
	}
	
	protected void startSubmission() {
		WebElement existing = null;
		List<WebElement> submissions = activityDetailPage.getSubmissions(sw);
		if (submissions != null) {
			for (WebElement subm : submissions) {// check the status of individual submission
				String submissionStatus = subm.findElement(Tools.getBy(".submission-title h2")).getAttribute("textContent");
				if ("In Progress".equals(submissionStatus) || "Done".equals(submissionStatus)) {
					existing = subm.findElement(Tools.getBy(".note"));
					break;
				}
			}
		}
		if (existing == null) {
			WebElement addSubmisisonBtn = activityDetailPage.getAddSubmissionBtn(sw);
			Assert.assertNotNull(addSubmisisonBtn);
			addSubmisisonBtn.click();
		} else {
			existing.click();
		}
		Tools.forceToWait(BuildConfig.jsWaitTime);
		waitForLoadFinished();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		assessmentsPage = new AssessmentsPage();
	}
	
	protected void uploadFile(String location, String[] evidenceFiles) {
		WebElement question = assessmentsPage.getQuestionGroup(sw, Integer.parseInt(location));
		question = scrollIfNotVisible(question);
		question.click();
		Tools.forceToWait(BuildConfig.pageWaitTime);
		int total = assessmentsGroupPage.getQuestions(assessmentsGroupPage.getGroupsQuestion(assessmentsGroupPage.getGroupPage(sw))).size();// include the title and descriptions
		for (int i = 1; i < total; i++) {
			WebElement uploadBtn = assessmentsGroupPage.getQuestion(
					assessmentsGroupPage.getGroupsQuestion(assessmentsGroupPage.getGroupPage(sw)), i)
					.findElement(Tools.getBy("file-question button.upload-button"));
			Assert.assertNotNull(uploadBtn);
			uploadBtn.click();
			Tools.forceToWait(BuildConfig.jsWaitTime);
			String mainWindowHandle = driver.getWindowHandle();
			TestLogger.trace(String.format("main window %s", mainWindowHandle));
			
			driver = dashboardActions.handleFileUpload(driver, sw, mainWindowHandle);
			Tools.forceToWait(BuildConfig.jsWaitTime);
			TestLogger.trace(String.format("window handles %s", Arrays.toString(driver.getWindowHandles().toArray())));
			
			try {
				dashboardActions.selectFile(sw, evidenceFiles[i - 1]);
				this.waitForFileUploading(3);
			} catch (Exception e) {
				TestLogger.error(Throwables.getStackTraceAsString(e));
			} finally {
				driver.switchTo().window(mainWindowHandle);// switch back
				Tools.forceToWait(BuildConfig.pageWaitTime);
			}
		}
		
		assessmentsGroupPage.save(assessmentsGroupPage.getGroupPage(sw));
		waitForLoadFinished();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		waitForLoadFinished();
		Tools.forceToWait(BuildConfig.jsWaitTime);
	}
	
	protected int checkAssessmentInformation(Questionnaire qn) {
		WebElement assessmentHeader = assessmentsPage.getAssessmentHeader(sw);
		Assert.assertNotNull(assessmentHeader);
		Assert.assertEquals(assessmentsPage.getAssessmentTitle(sw).getAttribute("innerText").toLowerCase(),
				qn.getName().toLowerCase());
		Assert.assertEquals(assessmentsPage.getAssessmentDescription(sw).getAttribute("innerText"),
				qn.getDescription());
		
		int numberOfAssessments = qn.getNumberOfAssessments();
		List<WebElement> questionGroups = assessmentsPage.getQuestionGroups(sw);
		Assert.assertNotNull(questionGroups);
		Assert.assertEquals(questionGroups.size(), numberOfAssessments);
		int i = 0;
		for (WebElement questionGroup : questionGroups) {
			String name = Tools.getElementTextContentWithSpecialChr(assessmentsPage.getQuestionGroupTitle(questionGroup));
			String description = Tools.getElementTextContentWithSpecialChr(assessmentsPage.getQuestionGroupDescription(questionGroup));
			Assert.assertEquals(name, qn.getAssessment(i).getName());
			Assert.assertEquals(description, qn.getAssessment(i).getDescription());
			i++;
		}
		return numberOfAssessments;
	}
	
	protected void doQuesitons(int start, int end, Questionnaire qn, boolean needToCheck, boolean doWork, boolean failed) {
		for (int j = start; j < end; j++) {
			assessmentsPage.getQuestionGroup(sw, j + 1).click();
			assessmentsGroupPage = new AssessmentsGroupPage();
			Tools.forceToWait(BuildConfig.jsWaitTime);
			WebElement assessmentsGPage = assessmentsGroupPage.getGroupPage(sw);
			Assert.assertNotNull(assessmentsGPage);
			WebElement groupsQuestion = assessmentsGroupPage.getGroupsQuestion(assessmentsGPage);
			Assert.assertNotNull(groupsQuestion);
			
			List<WebElement> questions = assessmentsGroupPage.getQuestions(groupsQuestion);
			int total = questions.size();
			Assert.assertNotNull(questions);
			Assert.assertEquals(assessmentsGroupPage.getAssessmentsGroupDescription(questions.get(0)).getAttribute("innerText"),
					qn.getAssessment(j).getDescription());
			for (int i = 1; i < total; i++) {
				WebElement question = assessmentsGroupPage.getQuestion(groupsQuestion, i);
				Question q = qn.getAssessment(j).getQuestion(i - 1);
				Assert.assertEquals(Tools.getElementTextContentWithSpecialChr(assessmentsGroupPage.getQuestionDescription(question)),
						q.getQcontent());
				if (needToCheck) {
					checkAnswer(q.getQtype(), question, q.getAnswer());
				}
				if (doWork) {
					completeQuestion(q, question, i, failed);
				}
			}
			if (doWork) {
				assessmentsGroupPage.save(assessmentsGPage);
			} else {
				assessmentsGroupPage.back(sw);
			}
			waitForLoadFinished();
			Tools.forceToWait(BuildConfig.jsWaitTime);
			waitForLoadFinished();
			Tools.forceToWait(BuildConfig.jsWaitTime);
		}
	}
	
	protected void startActivity(String location) {
		Tools.forceToWait(BuildConfig.jsWaitTime);
		WebElement fa = dashboardPage.getActScore(sw, Integer.parseInt(location));
		fa = scrollIfNotVisible(fa);
		fa.click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		activityDetailPage = new ActivityDetailPage();
	}

	protected void studentSubmitAssessment() {
		assessmentsPage.getSubmitBtn(sw).click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		waitForLoadFinished();
		WebElement dialog = sw.waitForElement("ion-alert[role=dialog]");
		Tools.forceToWait(BuildConfig.jsWaitTime);
		dialog.findElement(Tools.getBy(".alert-wrapper .alert-button-group button:nth-of-type(1)")).click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		waitForLoadFinished();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		waitForLoadFinished();
		Tools.forceToWait(BuildConfig.jsWaitTime);
	}
	
}
