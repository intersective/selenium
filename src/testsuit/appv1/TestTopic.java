package testsuit.appv1;


import java.util.ArrayList;
import java.util.List;

import model.MileStone;
import model.Topic;
import model.Topics;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.AssignmentDataService;
import service.Tools;
import testsuit.Appv1TestTemplate;

import common.BuildConfig;
import common.ElementType;


public class TestTopic extends Appv1TestTemplate {

	private Topics t;
	private String topicFile;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setTopicFile("topics_appv1student");
		setname("test appv1 topic");
		t = AssignmentDataService.getInstance().loadListDataFromJsonFiles(topicFile, 1, Topics.class).get(0);
	}
	
	@Test(description = "test topics for App v1", groups = "practera_appv1_assessment_topics")
	public void main() {
		MileStone mileStone = AssignmentDataService.getInstance().loadListDataFromJsonFiles("appv1_mileStones", 2, MileStone.class).get(0);
		WebElement currentAct = sw.waitForElement("//*[text()='Current Activity']/following-sibling::div", ElementType.XPATH);
		Assert.assertNotNull(currentAct);
		Assert.assertEquals(Tools.getElementTextContent(currentAct.findElement(Tools.getBy("p"))), mileStone.getName());
		Assert.assertEquals(Tools.getElementTextContent(currentAct.findElement(Tools.getBy(".card-time-point > span"))), mileStone.getStatus());
		
		findElement(".tab-nav > a:nth-of-type(2)").click();// a button can only click once in this app, so that we do this in the login test case
		waitForLoadFinished();
		Tools.forceToWait(2);
		
		List<WebElement> mileStones = sw.waitForListContent(".jsmbp-card-box");
		Assert.assertNotNull(mileStones);
		Tools.forceToWait(2);
		Assert.assertEquals(Tools.getElementTextContent(mileStones.get(1).findElement(Tools.getBy(".card-time-point"))), "- Locked - tap for details");
		mileStones.get(0).click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		
		checkTopics();
	}
	
	protected void checkTopics() {
		Tools.forceToWait(5);
		List<WebElement> assessments = sw.waitForListContent(".jsmbp-detail-items > div");
		Assert.assertNotNull(assessments);
		
		int i = 0;
		ArrayList<Topic> topcis = t.getTopics();
		for (Topic tp : topcis) {
			WebElement assessmentHeader = assessments.get(i).findElement(Tools.getBy(".item-content"));
			Assert.assertEquals(Tools.getElementTextContent(assessmentHeader.findElement(Tools.getBy("h3"))), tp.getTitle());
			Assert.assertEquals(Tools.getElementTextContent(assessmentHeader.findElement(Tools.getBy("p"))), "Topic");
			Assert.assertEquals(Tools.getElementTextContent(assessments.get(i).findElement(
					Tools.getBy(".item-btns > button"))).toLowerCase(), "view");
			assessments.get(i).click();
			
			waitForLoadFinished();
			sw.waitForElement(".activities");
			Assert.assertEquals(Tools.getElementTextContent(sw.waitForElement(".activities h3")), tp.getTitle());
			Assert.assertEquals(Tools.getElementTextContent(sw.waitForElement(".activities p")), tp.getDescription());
			sw.waitForElement(".nav-bar-block[nav-bar=active] .back-button").click();
			Tools.forceToWait(2);
			i++;
		}
	}

	public void setTopicFile(String topicFile) {
		this.topicFile = topicFile;
	}
	
}
