package listener;


import java.util.UUID;

import org.testng.IExecutionListener;

import service.ShareWebDriver;
import service.TestLogger;
import testsuit.JobsmartInitTest;

import common.ShareConfig;


public class InitJobSmartEnvironmentListener implements IExecutionListener {
	
	@Override
	public void onExecutionStart() {
		String localRunId = UUID.randomUUID().toString();
		System.out.println(String.format("local run id %s", localRunId));
		ShareConfig.seleniumRunId = localRunId;
		ShareConfig.systemName = "jobsmart";
		TestLogger.trace(String.format("Jobsmart test suites start at %s", System.currentTimeMillis()));
		(new JobsmartInitTest()).main();
	}

	@Override
	public void onExecutionFinish() {
		ShareWebDriver.getInstance().releaseResource();
		TestLogger.trace(String.format("JobSmart test suites finished at %s", System.currentTimeMillis()));
	}

}
