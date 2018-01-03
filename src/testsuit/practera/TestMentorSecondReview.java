package testsuit.practera;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class TestMentorSecondReview extends TestMentorReview {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test a mentor reviewing student submissions");
	}

	@Test(description = "test a mentor reviewing student submissions", groups = "practera_review_second_mentor",
			dependsOnGroups = "practera_review_second_assign")
	public void main() {
		super.main();
	}
	
}
