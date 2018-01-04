package listener;


import java.util.UUID;

import org.testng.IExecutionListener;

import service.ShareWebDriver;
import service.TestLogger;
import testsuit.Appv1InitTest;

import common.ShareConfig;


public class InitAppv1EnvironmentListener implements IExecutionListener {
	
	@Override
	public void onExecutionStart() {
		String localRunId = UUID.randomUUID().toString();
		System.out.println(String.format("local run id %s", localRunId));
		ShareConfig.seleniumRunId = localRunId;
		TestLogger.trace(String.format("Appv1 test suites start at %s", System.currentTimeMillis()));
		(new Appv1InitTest()).main();
	}

	@Override
	public void onExecutionFinish() {
		ShareWebDriver.getInstance().releaseResource();
		TestLogger.trace(String.format("Appv1 test suites finished at %s", System.currentTimeMillis()));
	}

}
