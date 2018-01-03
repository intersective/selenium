package service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import common.ShareConfig;


public class TestLogger {

	private static final Logger log = LogManager.getRootLogger();
	private static final Logger tracer = LogManager.getLogger("mytrace");
	private static final Logger info = LogManager.getLogger("myinfo");
	
	private TestLogger() {
	}
	
	public static void info(String message) {
		info.info(String.format("%s %s", ShareConfig.seleniumRunId, message));
	}
	
	public static void trace(String message) {
		tracer.trace(String.format("%s %s", ShareConfig.seleniumRunId, message));
	}
	
	public static void error(String message) {
		log.error(String.format("%s %s", ShareConfig.seleniumRunId, message));
	}
	
}
