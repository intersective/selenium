package testsuit.appv1.mentor;


import java.util.List;

import model.MileStone;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.AssignmentDataService;
import service.Tools;
import testsuit.Appv1TestTemplate;

import common.ElementType;


public class TestMentorTodo extends Appv1TestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test mentor to do list");
	}
	
	@Test(description = "test mentor to do list for App V1", groups = "practera_appv1_mentor_todo")
	public void main() {
		Tools.forceToWait(2);
		
		MileStone mileStone = AssignmentDataService.getInstance().loadListDataFromJsonFiles("appv1_mileStones", 2, MileStone.class).get(1);
		WebElement currentAct = sw.waitForElement("//*[text()='Current Activity']/following-sibling::div", ElementType.XPATH);
		Assert.assertNotNull(currentAct);
		Assert.assertEquals(Tools.getElementTextContent(currentAct.findElement(Tools.getBy("p"))), mileStone.getName());
		
		List<WebElement> mileStones = sw.waitForListContent(".jsmbp-card-box");
		Assert.assertNotNull(mileStones);
		Tools.forceToWait(2);
		Assert.assertEquals(Tools.getElementTextContent(mileStones.get(mileStones.size() - 1).findElement(Tools.getBy(".card-time-point"))), "- Unlocked! - tap for details");
		
		Tools.forceToWait(2);
	}

}
