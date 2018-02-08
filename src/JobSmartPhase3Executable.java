import java.io.File;
import java.util.List;

import org.testng.TestNG;

import com.beust.jcommander.internal.Lists;
import common.ShareConfig;


public class JobSmartPhase3Executable {
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("supply names for workflow file, must be start with item-");
		} else {
			if (!args[0].startsWith("item-")) {
				return;
			}
			
			ShareConfig.jobsmartWorkflow = args[0];
			TestNG testng = new TestNG();
	        List<String> suites = Lists.newArrayList();
	        suites.add(String.format("%s%s%s", System.getProperty("user.dir"), File.separator, "testsuit-practera_jobsmart-phase3.xml"));
	        testng.setTestSuites(suites);
	        testng.run();
		}
	}

}
