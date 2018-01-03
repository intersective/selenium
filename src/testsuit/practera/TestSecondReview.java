package testsuit.practera;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class TestSecondReview extends TestReview {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test assigning a mentor for reviewing student redo submissions");
	}

	@Test(description = "test assigning a mentor for reviewing student redo submissions", groups = "practera_review_second_assign")
	public void main() {
		super.main();
	}

}
