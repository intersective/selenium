package listener;


import org.testng.ISuite;
import org.testng.ISuiteListener;

import service.TestLogger;


public class TestSuiteListener implements ISuiteListener {

	@Override
	public void onStart(ISuite suite) {
		TestLogger.trace(String.format("(%s) start at %s", suite.getName(), System.currentTimeMillis()));
	}

	@Override
	public void onFinish(ISuite suite) {
		TestLogger.trace(String.format("(%s) finished at %s", suite.getName(), System.currentTimeMillis()));
	}

}
