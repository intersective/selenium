package testsuit.appv1.mentor;


import java.util.List;

import model.Assessment;
import model.Question;
import model.Questionnaire;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.AssignmentDataService;
import service.Tools;
import testsuit.Appv1TestTemplate;

import common.BuildConfig;
import common.appv1.OneofChoices;


public class TestMentorTodo extends Appv1TestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test mentor to do list");
	}
	
	@Test(description = "test mentor to do list for App V1", groups = "practera_appv1_mentor_todo")
	public void main() {
		Tools.forceToWait(2);
		List<WebElement> todoList = sw.waitForListContent(".reviews > div > .card");
		Assert.assertNotNull(todoList);
		
		Assessment assessment = AssignmentDataService.getInstance()
				.loadListDataFromJsonFiles("assessments_appv1student", 1, Questionnaire.class).get(0).getAssessment(2);
		String student = BuildConfig.appv1UserName.split("@")[0];
		
		boolean found = false;
		String assessmentName = null;
		WebElement viewBtn = null;
		for (WebElement todo : todoList) {
			String todoText[] = Tools.getElementTextContent(todo.findElement(Tools.getBy(".item:nth-of-type(2) div"))).split(" ");
			String studentName = todoText[0];
			if (student.equals(studentName)) {
				assessmentName = Tools.getElementTextContent(todo.findElement(Tools.getBy(".item:nth-of-type(1)")))
						.replace(Tools.getElementTextContent(todo.findElement(Tools.getBy(".item:nth-of-type(1) > span"))),"").trim();
				viewBtn = todo.findElement(Tools.getBy("button"));
				found = true;
				break;
			}
		}
		Assert.assertTrue(found);
		Assert.assertEquals(assessmentName, assessment.getName());
		Assert.assertEquals(Tools.getElementTextContent(viewBtn), "View");
		
		viewBtn.click();// assessment review detail page
		Tools.forceToWait(3);
		List<WebElement> assessmentMetaInfo = sw.waitForElement(".assessment-description").findElements(Tools.getBy("*"));
		Assert.assertEquals(Tools.getElementTextContent(assessmentMetaInfo.get(0)), assessment.getName());
		Assert.assertEquals(Tools.getElementTextContent(assessmentMetaInfo.get(1)), assessment.getDescription());
		
		List<WebElement> questionElements = sw.waitForListContent("question-text .item");
		Question question = assessment.getQuestion(0);
		Assert.assertEquals(Tools.getElementTextContent(questionElements.get(0)), question.getQcontent().split("\\.")[1].trim());
		Assert.assertEquals(Tools.getElementTextContent(questionElements.get(2)), question.getAnswer());
		Assert.assertEquals(Tools.getElementTextContent(questionElements.get(3)), "your works are excellent, thank you for submissions");
		
		List<WebElement> oneofElements = sw.waitForListContent(".oneof .item");
		Question question2 = assessment.getQuestion(1);
		Assert.assertEquals(Tools.getElementTextContent(oneofElements.get(0)), question2.getQcontent().split("\\.")[1].trim());
		Assert.assertEquals(OneofChoices.valueOf(Tools.getElementTextContent(oneofElements.get(1)).toUpperCase()).getOrder(),
				Integer.parseInt(question2.getAnswer()));
		Assert.assertEquals(OneofChoices.valueOf(Tools.getElementTextContent(oneofElements.get(2)).toUpperCase()).getOrder(), OneofChoices.GOOD.getOrder());
		sw.waitForElement(".pane[nav-view='active'] .back-button").click();
		
		todoList = sw.waitForListContent(".reviews > div > .card");
		Tools.forceToWait(2);
	}

}
