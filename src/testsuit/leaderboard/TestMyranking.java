package testsuit.leaderboard;


import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalStore;
import testsuit.TestTemplate;

import common.ElementType;


public class TestMyranking extends TestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test my ranking data");
	}
	
	@Test(description = "test my ranking data", groups = "myranking", dependsOnGroups = "first")
	public void main() {
		LocalStore ls = LocalStore.getInstance();
		waitForLoadFinished();
		
		WebElement tab = sw.waitForElement("tab-t0-2", ElementType.ID);
		Assert.assertNotNull(tab);
		tab.click();
		waitForLoadFinished();
		Assert.assertNotNull(sw.waitForElement("#tabpanel-t0-2[aria-hidden=false]"));
		
		WebElement myranking = sw.waitForElement("my-ranking", ElementType.CLASSNAME);
		Assert.assertNotNull(myranking);
		Object ipoints = runJSScript("return document.querySelector('.my-ranking .item-inner').children[1].textContent");
		Assert.assertNotNull(ipoints);
		if (ls.getUserPotins() != null) {
			Assert.assertEquals(((String) ipoints).replaceAll("\n", "").trim(), ls.getUserPotins());
		}
	}

}
