package testsuit;


import model.User;
import respositry.LocalDataBase;

import common.BuildConfig;


public class JobsmartInitTest extends InitTest {
	
	@Override
	public void main() {
		super.main();
	}

	@Override
	protected void initUserData() {
		LocalDataBase ldb = LocalDataBase.getInstance();
		User t;
		if ((t = ldb.getCurrnetUser(BuildConfig.jobsmartId)) != null) {
			BuildConfig.jobsmartStudent = t.getUserName();
		}
	}
	
}
