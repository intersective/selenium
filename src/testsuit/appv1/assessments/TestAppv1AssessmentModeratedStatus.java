package testsuit.appv1.assessments;


import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;

import common.BuildConfig;
import common.appv1.AssessmentStatus;


public class TestAppv1AssessmentModeratedStatus extends TestAppv1AssessmentModerated {

	private String estatus;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test appv1 moderated assessment status");
		setEstatus(AssessmentStatus.PENDINGAPPROVAL.getText());
	}

	@Test(description = "test appv1 moderated assessment status", groups = "practera_appv1_assessment_moderated_status")
	public void main() {
		findElement(".tab-nav > a:nth-of-type(2)").click();
		waitForLoadFinished();
		Tools.forceToWait(2);
		
		List<WebElement> mileStones = sw.waitForListContent(".jsmbp-card-box");
		Assert.assertNotNull(mileStones);
		Tools.forceToWait(2);
		mileStones.get(0).click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		
		checkStatus();
	}

	@Override
	protected void checkStatus() {
		String status = Tools.getElementTextContent(sw.waitForElement(String
				.format(".jsmbp-detail-items > div:nth-of-type(%s) > .item p[ng-if=status]", getAssessmentLocation() + 2))).toLowerCase();
		Assert.assertEquals(status, estatus);
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

}
