package listener;


import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import service.TestLogger;

import common.ShareConfig;


public class TestResultListener extends TestListenerAdapter {

	public TestResultListener() {
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		super.onTestSuccess(tr);
		TestLogger.trace(String.format("success %s %s", tr.getEndMillis(),tr.toString()));
		if ("testsuit.TestLogin".equals(tr.getInstanceName()) || "testsuit.appv1.TestAppv1Login".equals(tr.getInstanceName())) {
			ShareConfig.isTestLoginRun = true;
		} else if ("testsuit.TestLogout".equals(tr.getInstanceName())) {
			ShareConfig.isTestLoginRun = false;
		}
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		super.onTestFailure(tr);
		TestLogger.trace(String.format("failure %s %s", tr.getEndMillis(),tr.toString()));
		if ("testsuit.TestLogin".equals(tr.getInstanceName()) || "testsuit.appv1.TestAppv1Login".equals(tr.getInstanceName())) {
			ShareConfig.isTestLoginRun = false;
		}
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		super.onTestSkipped(tr);
		TestLogger.trace(String.format("skipped %s %s", tr.getEndMillis(),tr.toString()));
	}
	
}
