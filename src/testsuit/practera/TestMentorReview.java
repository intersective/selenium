package testsuit.practera;


import java.util.ArrayList;
import java.util.List;

import model.Questionnaire;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.AssignmentDataService;
import service.PageActionFactory;
import service.Tools;
import testsuit.TestTemplate;
import testsuit.practera.actions.Actions;

import common.BuildConfig;
import common.ElementType;


public class TestMentorReview extends TestTemplate {

	protected ArrayList<String> assessments;
	protected Actions actions;
	private String userName;
	private String loginUserName;
	private String loginPassword;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setUserName(BuildConfig.userName.split("@")[0]);
		setLoginUserName(BuildConfig.practerMentor);
		setLoginPassword(BuildConfig.practeraMentorPassword);
		setname("test a mentor reviewing student submissions");
		initAssessmentData();
		
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.practera.actions.Actions");
	}

	@Test(description = "test a mentor reviewing student submissions", groups = "practera_review_mentor",
			dependsOnGroups = "practera_review_assign")
	public void main() {
		driver.get(BuildConfig.practeraUrl);
		actions.login(sw, loginUserName, loginPassword);
		
		WebElement sideBar = actions.getSidebar(sw);
		WebElement myReview = sideBar.findElement(Tools.getBy("ul.nav li:nth-of-type(4)"));
		myReview.findElement(Tools.getBy(ElementType.TAGNAME, "a")).click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		sw.waitForElement("ul.nav li:nth-of-type(4) > ul > li > a").click();
		
		int i = 2;// always starts from the second row which is the actual data because the first row only contains the headers
		int total = actions.waitForReviewContent(sw) + 1;
		while (i != total && total != 1) {// we have not finished all reviews
			WebElement row = sw.waitForElement(String.format("div.page-content > div.content-container > div#assessments > div.tab-content > div#toreview > div.row:nth-of-type(%s)", i));
			if (userName.equals(Tools.getElementTextContent(row.findElement(Tools.getBy("div:nth-of-type(3)")))) &&
					assessments.contains(Tools.getElementTextContent(row.findElement(Tools.getBy("div:nth-of-type(1)"))))) {
				row.findElement(Tools.getBy("div:nth-of-type(4) > a")).click();
				sw.waitForElement("div#start-page > div.form-actions > button").click();
				
				review();
				total = actions.waitForReviewContent(sw) + 1;
				i = 2;
			} else {
				++i;
			}
		}
		
		actions.waitToastMessageDisappear(sw);
		logout();
	}
	
	protected void processToNext() {
		WebElement nextBtn = sw.waitForElement("div.content-container > div#assessment-buttons > div > div:nth-of-type(2) > button.btn-primary");
		nextBtn = scrollIfNotVisible(nextBtn);
		nextBtn.click();
		actions.waitToastMessageDisappear(sw);
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	protected void initAssessmentData() {
		assessments = new ArrayList<String>();
		ArrayList<Questionnaire> qns = AssignmentDataService.getInstance().loadListDataFromJsonFiles("assessments_student", 6, Questionnaire.class);
		for (Questionnaire q : qns) {
			assessments.add(q.getName());
		}
	}

	/**
	 * the actual actions for reviewing the assessments
	 */
	protected void review() {
		Tools.forceToWait(BuildConfig.pageWaitTime);
		Assert.assertNotNull(sw.waitForElement("div#assessment > div.page-header > div > ul.wizard-steps > li:nth-of-type(1).active"));
		List<WebElement> comments = sw.waitForListContent("div#assessment > div.content-container > div.question > div > div.choice");
		String finalGrade;
		WebElement firstAnswer = findElement(comments.get(0),"p");
		if (firstAnswer == null) {
			firstAnswer = findElement(comments.get(0),"textArea");
			if (firstAnswer == null) {
				finalGrade = "2";
			} else {
				finalGrade = Tools.getElementContainText(firstAnswer).contains("more time") ? "5" : "2";
			}
		} else {
			finalGrade = Tools.getElementTextContent(firstAnswer).contains("more time") ? "5" : "2";
		}
		
		WebElement textArea;
		if (comments.size() > 1) {
			textArea = comments.get(1).findElement(Tools.getBy(ElementType.TAGNAME, "textarea"));
			textArea = scrollIfNotVisible(textArea);
			textArea.sendKeys(new String[] { "your works are excellent, thank you for submissions" });
		}
		if (comments.size() > 2) {
			textArea = comments.get(2).findElement(Tools.getBy(ElementType.TAGNAME, "textarea"));
			textArea = scrollIfNotVisible(textArea);
			textArea.sendKeys(new String[] { "your works are excellent, thank you for submissions" });
		}
		fillReviwerQuestion("quite close to the example answer", "thank you for submissions", 2);
		processToNext();
		
		Tools.forceToWait(BuildConfig.pageWaitTime);
		sw.waitForElement("div#assessment > div.page-header > div > ul.wizard-steps > li:nth-of-type(2).active");
		fillReviwerQuestion("quite close to the example answer", "thank you for submissions", 2);
		fillReviwerQuestion("quite close to the example answer", "thank you for submissions", 3);
		fillReviwerQuestion("quite close to the example answer", "thank you for submissions", 4);
		fillReviwerQuestion("quite close to the example answer", "thank you for submissions", 5);
		fillReviwerQuestion("quite close to the example answer", "thank you for submissions", 6);
		fillReviwerQuestion("quite close to the example answer", "thank you for submissions", 8);
		fillReviwerQuestion("quite close to the example answer", "thank you for submissions", 10);
		fillReviwerQuestion("quite close to the example answer", "thank you for submissions", 12);
		processToNext();
		
		for (int j = 3; j < 5; j++) {
			sw.waitForElement(String.format("div#assessment > div.page-header > div > ul.wizard-steps > li:nth-of-type(%s).active", j));
			processToNext();
		}
		
		sw.waitForElement("div#assessment > div.page-header > div > ul.wizard-steps > li:nth-of-type(5).active");
		findElement(String.format("div.content-container > div.question > div > div.choice-oneof > div.radio:nth-of-type(%s) input[type=radio] + span", finalGrade)).click();
		findElement("div.content-container > div.question > div > div#choice-comment > textarea")
				.sendKeys(new String[] { "your works are excellent, thank you for submissions" });
		processToNext();
	}

	protected void logout() {
		actions.logout(sw, "3");
	}
	
	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	
	protected void fillComment(String comment) {
		WebElement commentTextArea = sw.waitForElement("#choice-comment > textarea");
		commentTextArea = scrollIfNotVisible(commentTextArea);
		commentTextArea.sendKeys(new String[] { comment });
	}
	
	private void fillReviwerQuestion(String answer, String comment, int questionIndex) {
		WebElement one = findElement(String.format("textarea[id='AssessmentReviewAnswer[%s]Answer']", questionIndex));
		if (one != null) {
			one = scrollIfNotVisible(one);
			one.sendKeys(new String [] { answer });
		}
		
		WebElement two = findElement(String.format("textarea[id='AssessmentReviewAnswer%sComment']", questionIndex));
		if (two != null) {
			two = scrollIfNotVisible(two);
			two.sendKeys(new String [] { comment });
		}
	}
	
}
