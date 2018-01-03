import java.io.File;
import java.util.List;

import org.testng.TestNG;

import com.beust.jcommander.internal.Lists;


public class Executable {
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("supply a test suite xml file");
		} else {
			if (!args[0].endsWith(".xml")) {
				System.out.println("supply a valid test suite xml file");
			} else {
				TestNG testng = new TestNG();
		        List<String> suites = Lists.newArrayList();
		        suites.add(String.format("%s%s%s", System.getProperty("user.dir"), File.separator, args[0]));
		        testng.setTestSuites(suites);
		        testng.run();
			}
		}
	}

}
