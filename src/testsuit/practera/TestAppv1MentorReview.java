package testsuit.practera;


import java.util.ArrayList;
import java.util.List;

import model.Questionnaire;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.AssignmentDataService;
import service.Tools;
import common.BuildConfig;
import common.ElementType;


public class TestAppv1MentorReview extends TestMentorReview {

	@BeforeClass
	public void setup() {
		super.setup();
		setUserName(BuildConfig.appv1UserName.split("@")[0]);
		setLoginUserName(BuildConfig.appV1Mentor);
		setLoginPassword(BuildConfig.appV1MentorPassword);
		setname("test a mentor reviewing student submissions from app v1");
	}

	@Test(description = "test a mentor reviewing student submissions from app v1", groups = "practera_review_mentor_appv1",
			dependsOnGroups = "practera_review_assign_appv1")
	public void main() {
		super.main();
	}

	@Override
	protected void initAssessmentData() {
		assessments = new ArrayList<String>();
		ArrayList<Questionnaire> qns = AssignmentDataService.getInstance().loadListDataFromJsonFiles("assessments_appv1student", 1, Questionnaire.class);
		assessments.add(qns.get(0).getAssessment(2).getName());
	}

	@Override
	protected void review() {
		Assert.assertNotNull(sw.waitForElement("div#assessment > div.page-header > div > ul.wizard-steps > li:nth-of-type(1).active"));
		List<WebElement> comments = findElements("div#assessment > div.content-container > div.question > div > div.choice");
		WebElement textArea = comments.get(1).findElement(Tools.getBy(ElementType.TAGNAME, "textarea"));
		Assert.assertNotNull(textArea);
		textArea.sendKeys(new String[] { "your works are excellent, thank you for submissions" });
		fillComment("your works are excellent, thank you for submissions");
		processToNext();
		
		sw.waitForElement(String.format("div#assessment > div.page-header > div > ul.wizard-steps > li:nth-of-type(%s).active", 2));
		sw.waitForElement("//input[@name='data[AssessmentReviewAnswer][2][answer]']/../span", ElementType.XPATH).click();
		fillComment("your works are excellent, thank you for submissions");
		processToNext();
		
		sw.waitForElement(String.format("div#assessment > div.page-header > div > ul.wizard-steps > li:nth-of-type(%s).active", 3));
		List<WebElement> checkbox = sw.waitForListContent(".checkbox > label > span");
		checkbox.get(2).click();
		checkbox.get(3).click();
		fillComment("your works are excellent, thank you for submissions");
		processToNext();
		
		sw.waitForElement(String.format("div#assessment > div.page-header > div > ul.wizard-steps > li:nth-of-type(%s).active", 4));
		Tools.forceToWait(1);
		processToNext();
		
		sw.waitForElement(String.format("div#assessment > div.page-header > div > ul.wizard-steps > li:nth-of-type(%s).active", 5));
		Tools.forceToWait(1);
		fillComment("your works are excellent, thank you for submissions");
		processToNext();
	}

	@Override
	protected void logout() {
		actions.logout(sw, "2");
	}
	
}
