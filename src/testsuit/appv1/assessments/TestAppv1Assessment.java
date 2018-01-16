package testsuit.appv1.assessments;


import java.util.List;

import model.Assessment;
import model.Question;
import model.Questionnaire;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import service.AssignmentDataService;
import service.Tools;
import testsuit.Appv1TestTemplate;

import common.BuildConfig;
import common.ElementType;


public abstract class TestAppv1Assessment extends Appv1TestTemplate {

	private Questionnaire questionare;
	private int assessmentLocation;
	protected boolean doFileQuestions = true;
	
	@BeforeClass
	public void setup() {
		super.setup();
		questionare = AssignmentDataService.getInstance().loadListDataFromJsonFiles("assessments_appv1student", 1, Questionnaire.class).get(0);
	}

	@Override
	public void main() {
		List<WebElement> assessments = sw.waitForListContent(".jsmbp-detail-items > div");
		Assert.assertNotNull(assessments);
		Assert.assertEquals(assessments.size() - 1, questionare.getNumberOfAssessments());// Exclude the topic
		
		Assert.assertEquals(Tools.getElementTextContent(sw.waitForElement(".jsmbp-detail-container > div > .item > p:nth-of-type(1)")), questionare.getName());
		Assert.assertEquals(Tools.getElementTextContent(sw.waitForElement(".jsmbp-detail-container > div > .item > p:nth-of-type(2)")), questionare.getDescription());
		
		Assessment assessment = questionare.getAssessment(assessmentLocation);
		WebElement assessmentHeader = assessments.get(assessmentLocation + 1).findElement(Tools.getBy(".item-content"));
		Assert.assertEquals(Tools.getElementTextContent(assessmentHeader.findElement(Tools.getBy("h3"))), assessment.getName());
		Assert.assertEquals(Tools.getElementTextContent(assessmentHeader.findElement(Tools.getBy("p"))), "Assessment");
		Assert.assertEquals(Tools.getElementTextContent(assessments.get(assessmentLocation + 1).findElement(
								Tools.getBy(".item-btns > button"))).toLowerCase(), "do now");
		assessments.get(assessmentLocation + 1).click();
		
		waitForLoadFinished();
		sw.waitForElement(".activities");
		Assert.assertEquals(Tools.getElementTextContent(sw.waitForElement("div[ng-if='assessment.name'] > h2")), assessment.getName());
		Assert.assertEquals(Tools.getElementTextContent(sw.waitForElement("div[ng-if='assessment.description']")), assessment.getDescription());
		
		List<WebElement> questions = sw.waitForListContent("div[ng-repeat='question in group.questions']");
		int i = 0;
		for (WebElement q : questions) {
			Question qu = assessment.getQuestion(i);
			String title = Tools.getElementTextContent(q.findElement(Tools.getBy(".item h2")));
			Assert.assertEquals(title, qu.getQcontent());
			WebElement answerContainer;
			WebElement description = findElement(q, "div[ng-if='question.description']");
			if (description != null) {
				answerContainer = q.findElement(Tools.getBy("div:nth-of-type(3)"));
				Assert.assertEquals(Tools.getElementTextContent(description), qu.getDescription());
			} else {
				answerContainer = q.findElement(Tools.getBy("div:nth-of-type(2)"));
			}
			answerContainer = scrollIfNotVisible(answerContainer);
			doQuestion(answerContainer, qu.getAnswer());
			i++;
		}
		Tools.forceToWait(1);
		
		submit();
		checkStatus();
	}
	
	private void doQuestion(WebElement q, String answer) {
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
		} else if (doFileQuestions) {
			if (type.contains("file")) {
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

	protected void submit() {
		waitForVisibleWithScroll("//button[text()='Submit']", ElementType.XPATH).click();
		WebElement submitConfirm = sw.waitForElement(".popup");
		Assert.assertEquals(Tools.getElementTextContent(submitConfirm.findElement(Tools.getBy(".popup-head > .popup-title"))), "Confirm submission");
		Assert.assertEquals(Tools.getElementTextContent(submitConfirm.findElement(Tools.getBy(".popup-body"))), "Are you sure that you wish to submit? All submissions are final. No resubmissions will be accepted.");
		submitConfirm.findElement(Tools.getBy(".popup-buttons > button:nth-of-type(1)")).click();
		Tools.forceToWait(1);
		
		waitForVisibleWithScroll("//button[text()='Submit']", ElementType.XPATH).click();
		submitConfirm = sw.waitForElement(".popup");
		submitConfirm.findElement(Tools.getBy(".popup-buttons > button:nth-of-type(2)")).click();
		
		try{
			Assert.assertNotNull(sw.waitForElement("//*[@class='popup-title'][text()='Submission Successful!']", ElementType.XPATH, 60));
			Tools.forceToWait(2);
			findElement(".popup> .popup-buttons > button").click();
			Tools.forceToWait(2);
			Assert.assertNotNull(sw.waitForElement("//*[@class='congrate-header'][text()='Submission Successful']", ElementType.XPATH, 60));
			sw.waitForElement(".modal button").click();
			Tools.forceToWait(BuildConfig.jsWaitTime);
			
			Assert.assertEquals(Tools.getElementTextContent(
					sw.waitForElement(String.format(".jsmbp-detail-items > div:nth-of-type(%s) .item-btns > button", assessmentLocation + 2))).toLowerCase(), "view");
		} catch (Error e) {
			findElement(".popup> .popup-buttons > button").click();// a window pops up only if there is a submission error
			throw new AssertionError(e);
		}
	}

	protected abstract void checkStatus();
	
	public void setAssessmentLocation(int assessmentLocation) {
		this.assessmentLocation = assessmentLocation;
	}

	public int getAssessmentLocation() {
		return assessmentLocation;
	}
	
}
