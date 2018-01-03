package testsuit.practera;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class TestPublishSecondReview extends TestPublishReview {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test assigning a mentor for reviewing student submissions");
	}

	@Test(description = "test assigning a mentor for reviewing student submissions", groups = "practera_review_second_publish",
			dependsOnGroups = "practera_review_second_mentor")
	public void main() {
		super.main();
	}

}
