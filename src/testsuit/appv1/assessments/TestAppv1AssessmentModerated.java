package testsuit.appv1.assessments;


import model.MileStone;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.AssignmentDataService;
import service.Tools;
import service.UIAction;

import common.ElementType;
import common.appv1.AssessmentStatus;


public class TestAppv1AssessmentModerated extends TestAppv1Assessment {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test appv1 moderated questions");
		setAssessmentLocation(2);
		doFileQuestions = false;
	}

	@Test(description = "test moderated questions for App v1", groups = "practera_appv1_assessment_moderated")
	public void main() {
		super.main();
		UIAction.waitForElementVisible(sw, ".nav-bar-block[nav-bar=active] button[ng-click='goBack()']").click();// back to the milestone page
		Tools.forceToWait(2);
		
		findElement(".tab-nav > a:nth-of-type(1)").click();
		waitForLoadFinished();
		Tools.forceToWait(2);
		
		MileStone mileStone = AssignmentDataService.getInstance().loadListDataFromJsonFiles("appv1_mileStones", 2, MileStone.class).get(1);
		WebElement currentAct = sw.waitForElement("//*[text()='Current Activity']/following-sibling::div", ElementType.XPATH);
		Assert.assertNotNull(currentAct);
		Assert.assertEquals(Tools.getElementTextContent(currentAct.findElement(Tools.getBy("p"))), mileStone.getName());
		Assert.assertEquals(Tools.getElementTextContent(currentAct.findElement(Tools.getBy(".card-time-point > span"))),mileStone.getStatus());
		
		findElement(".tab-nav > a:nth-of-type(2)").click();
		waitForLoadFinished();
		Tools.forceToWait(2);
	}

	@Override
	protected void checkStatus() {
		String status = Tools.getElementTextContent(sw.waitForElement(String
				.format(".jsmbp-detail-items > div:nth-of-type(%s) > .item p[ng-if=status]", getAssessmentLocation() + 2))).toLowerCase();
		Assert.assertEquals(status, AssessmentStatus.PENDINGREVIEW.getText());
	}
	
}
