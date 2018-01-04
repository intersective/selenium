package testsuit;


import model.User;
import respositry.LocalDataBase;

import common.BuildConfig;


public class Appv1InitTest extends InitTest {
	
	@Override
	public void main() {
		super.main();
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
