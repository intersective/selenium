package listener;


import java.util.UUID;

import org.testng.IExecutionListener;

import service.ShareWebDriver;
import service.TestLogger;
import testsuit.InitTest;

import common.ShareConfig;


public class InitPEEnvironmentListener implements IExecutionListener {
	
	@Override
	public void onExecutionStart() {
		String localRunId = UUID.randomUUID().toString();
		System.out.println(String.format("local run id %s", localRunId));
		ShareConfig.seleniumRunId = localRunId;
		ShareConfig.systemName = "pe";
		TestLogger.trace(String.format("Personal Edge test suites start at %s", System.currentTimeMillis()));
		(new InitTest()).main();
	}

	@Override
	public void onExecutionFinish() {
		ShareWebDriver.getInstance().releaseResource();
		TestLogger.trace(String.format("Personal Edge test suites finished at %s", System.currentTimeMillis()));
	}

}
