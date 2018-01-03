package testsuit.spinner;


import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalStore;
import service.Tools;
import testsuit.TestTemplate;

import common.ElementType;


public class TestSpinWheel extends TestTemplate {
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test spinning the wheel");
	}

	@Test(description = "test spinning the wheel and check points and spin chances update correctly", groups = "spinwheel",
			dependsOnGroups = "spinnerboard_laytout")
	public void main() {
		LocalStore ls = LocalStore.getInstance();
		WebElement tabpanel = sw.waitForElement("#tabpanel-t0-3[aria-hidden=false]");
		List<WebElement> lis = tabpanel.findElements(Tools.getBy("ion-grid[_ngcontent-c1] ul li"));
		
		int tabSpinNum = Integer.parseInt(Tools.getElementTextContent(sw.waitForElement("#tab-t0-3 ion-badge")));
		int spinChances = Integer.parseInt(Tools.getElementTextContent(lis.get(0).findElement(Tools.getBy("p#spinChances"))));
		Assert.assertTrue(tabSpinNum == spinChances);
		String spinEP = Tools.getElementTextContent(lis.get(1).findElement(Tools.getBy("p#spinEP")));
		if (ls.getUserPotins() != null) {
			Assert.assertEquals(ls.getUserPotins(), spinEP);
		}
		
		WebElement spinWheel = tabpanel.findElement(Tools.getBy("canvas#spinwheel"));
		spinWheel.click();
		
		WebElement dialog = sw.waitForElement("ion-alert[role=dialog]", ElementType.CSSSELECTOR, 20);
		Assert.assertEquals(Tools.getElementTextContent(dialog.findElement(Tools.getBy("h2.alert-title"))),
				"Congratulations");
		String subTitle = Tools.getElementTextContent(dialog.findElement(Tools.getBy("h3.alert-sub-title")));
		int increment = Tools.extractNumberFromString(subTitle);
		Assert.assertTrue(increment != -1);
		Tools.forceToWaitInMilli(500);
		Tools.runJSScript(driver, "document.querySelector('ion-alert[role=dialog] ion-backdrop').click();");// the backdrop laid behind the wheel, only JS works for clicking
		
		String aspinEP = Tools.getElementTextContent(lis.get(1).findElement(Tools.getBy("p#spinEP")));
		Assert.assertTrue(Integer.parseInt(spinEP) + increment == Integer.parseInt(aspinEP));
		ls.addUserPoints(aspinEP);
		
		WebElement ionBadge = sw.waitForElement("#tab-t0-3 ion-badge");
		int atabSpinNum = 0;
		if (ionBadge != null) {
			atabSpinNum = Integer.parseInt(Tools.getElementTextContent(ionBadge));
		}
		int aspinChances = Integer.parseInt(Tools.getElementTextContent(lis.get(0).findElement(Tools.getBy("p#spinChances"))));
		Assert.assertTrue(tabSpinNum - atabSpinNum == 1);
		Assert.assertTrue(spinChances - aspinChances == 1);
		Assert.assertTrue(tabSpinNum == spinChances);
		ls.addSpinChances(String.valueOf(spinChances));
	}

}
