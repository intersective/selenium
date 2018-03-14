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
		WebElement currentAct = sw.waitForElement("//*[text()='Things to do']/following-sibling::div", ElementType.XPATH);
		Assert.assertNotNull(currentAct);
		Assert.assertEquals(Tools.getElementTextContent(findElement(currentAct, ".title")), mileStone.getName());
		Assert.assertEquals(Tools.getElementTextContent(findElement(currentAct, "h3")), mileStone.getStatus());
		
		findElement(".tab-nav > a:nth-of-type(2)").click();// a button can only click once in this app, so that we do this in the login test case
		waitForLoadFinished();//we go to the activities page
		Tools.forceToWait(2);
		
		List<WebElement> mileStones = sw.waitForListContent(".view-container[nav-view='active'] .card");
		Assert.assertNotNull(mileStones);
		Tools.forceToWait(2);
		Assert.assertNull(findElement(mileStones.get(1),"h3"));// would not have the test if it is locked
		mileStones.get(0).click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		
		checkTopics();
	}
	
	/**
	 * the topics always sit on the front of the activity content list
	 */
	protected void checkTopics() {
		Tools.forceToWait(5);
		List<WebElement> assessments = sw.waitForListContent(".jsmbp-detail-items > div");
		Assert.assertNotNull(assessments);
		
		int i = 0;
		ArrayList<Topic> topcis = t.getTopics();
		for (Topic tp : topcis) {
			WebElement assessmentHeader = findElement(assessments.get(i), ".item");
			Assert.assertEquals(Tools.getElementTextContent(findElement(assessmentHeader,"detail-title h2")), tp.getTitle());
			Assert.assertEquals(Tools.getElementTextContent(findElement(assessmentHeader, "detail-title p")).toLowerCase(), "topic");
			assessments.get(i).click();
			
			waitForLoadFinished();
			sw.waitForElement(".pane[nav-view='active'] .activities");
			Assert.assertEquals(Tools.getElementTextContent(sw.waitForElement(".pane[nav-view='active'] .activities h3")), tp.getTitle());
			Assert.assertEquals(Tools.getElementTextContent(sw.waitForElement(".pane[nav-view='active'] .activities .item-body")), tp.getDescription());
			sw.waitForElement(".nav-bar-block[nav-bar=active] .back-button").click();
			Tools.forceToWait(2);
			i++;
		}
	}

	public void setTopicFile(String topicFile) {
		this.topicFile = topicFile;
	}
	
}
