package testsuit;


import model.User;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalDataBase;
import respositry.LocalStore;
import common.BuildConfig;


public class Appv1InitTest extends InitTest {
	
	@BeforeClass
	public void setup() {
		super.setup();
	}

	@Test(description = "test initialisation")
	public void main() {
		LocalStore ls = LocalStore.getInstance();
		if ("true".equals(ls.getValue("InitTest"))) {
			return;
		}
		ls.addValue("InitTest", "true");
		
		super.init();
		this.initUserData();
	}

	@Override
	protected void initUserData() {
		LocalDataBase ldb = LocalDataBase.getInstance();
		User t;
		if ((t = ldb.getCurrnetUser(BuildConfig.appv1Id)) != null) {
			BuildConfig.appv1UserName = t.getUserName();
		}
	}
	
	
}
