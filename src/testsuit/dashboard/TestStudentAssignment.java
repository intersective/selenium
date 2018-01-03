package testsuit.dashboard;


import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import model.Questionnaire;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalDataBase;
import respositry.LocalStore;
import service.AssignmentDataService;
import service.TestLogger;
import service.Tools;
import service.UIAction;

import com.google.common.base.Throwables;

import common.BuildConfig;
import common.ElementType;


public class TestStudentAssignment extends AssignmentTestTemplate {

	protected ArrayList<Questionnaire> qns;
	private LocalStore ls;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test of doing an assignment for a new student");
		qns = AssignmentDataService.getInstance().loadListDataFromJsonFiles("assessments_student", 7, Questionnaire.class);
		ls = LocalStore.getInstance();
	}

	@Test(description = "do an assignment for a student, we will fail two submissions this time", groups = "dashboard_student_assginment", dependsOnGroups = "dashboard_student")
	public void main() {
		int i = 0;
		doAssignment(i, false);
		i = doAssignment(i, true);
		
		doAssignment(i, false);
		i = doAssignment(i, false);
		
		doAssignment(i, false);
		i = doAssignment(i, false);
		
		doAssignment(i, false);
		i = doAssignment(i, false);
		
		doAssignment(i, false);
		i = doAssignment(i, true);
		
		doAssignment(i, false);
		i = doAssignment(i, false);
		
		Tools.forceToWait(BuildConfig.jsWaitTime);
	}

	/**
	 * 
	 * @param i - location of the activity
	 * @param failed - whether to use a failed answer
	 */
	protected void addOneSubmission(int i, boolean failed) {
		Questionnaire qn = qns.get(i - 1);
		String activityName = Tools.getElementTextContent(dashboardPage.getActTitle(sw, i));
		
		startActivity(String.valueOf(i));
		startSubmission();
		checkAssessmentInformation(qn);
		doQuesitons(0, 2, qn, false, true, failed);
		String[] evidencesFiles = new String[2];
		evidencesFiles[0] = String.format("%s%s%s", BuildConfig.evidenceFolder, File.separator, qn.getAssessment(2).getQuestion(0).getAnswer());
		uploadFile("3", evidencesFiles);
		evidencesFiles[0] = String.format("%s%s%s", BuildConfig.evidenceFolder, File.separator, qn.getAssessment(3).getQuestion(0).getAnswer());
		evidencesFiles[1] = String.format("%s%s%s", BuildConfig.evidenceFolder, File.separator, qn.getAssessment(3).getQuestion(1).getAnswer());
		uploadFile("4", evidencesFiles);
		
		assessmentsPage.back(sw);
		waitForLoadFinished();
		Tools.forceToWait(1);
		activityDetailPage.back(sw);
		Tools.forceToWait(BuildConfig.jsWaitTime);
		waitForLoadFinished();
		Tools.forceToWait(1);
		
		startActivity(String.valueOf(i));
		startSubmission();
		checkAssessmentInformation(qn);
		doQuesitons(0, 2, qn, true, true, failed);
		studentSubmitAssessment();
		Calendar current = Calendar.getInstance();
		String submissionTime = String.format("%s-%s-%s-%s-%s-%s", current.get(Calendar.YEAR), current.get(Calendar.MONTH) + 1, current.get(Calendar.DAY_OF_MONTH), 
	    		current.get(Calendar.HOUR_OF_DAY), current.get(Calendar.MINUTE), current.get(Calendar.SECOND));
		LocalDataBase.getInstance().addUserSubmission(BuildConfig.userName, activityName, submissionTime);
	}
	
	/**
	 * 
	 * @param i
	 * @param failed - whether to use a failed answer
	 * @return
	 */
	private int doAssignment(int i, boolean failed) {
		++i;
		LocalDataBase ldb = LocalDataBase.getInstance();
		String activityName = Tools.getElementTextContent(dashboardPage.getActTitle(sw, i));
		
		int submissionNumber = ldb.getUserSubmissionNumber(BuildConfig.userName, activityName);
		List<WebElement> ticks = dashboardPage.getActTicks(sw, i);
		if (submissionNumber < 1) {
			for (WebElement t : ticks) {
				Assert.assertFalse(t.getAttribute("class").contains("fa-check-circle-o"));// not ticked
			}
		} else {
			checkTicks(ticks, submissionNumber);
		}
		
		addOneSubmission(i, failed);
		
		submissionNumber = ldb.getUserSubmissionNumber(BuildConfig.userName, activityName);
		ticks = dashboardPage.getActTicks(sw, i);
		checkTicks(ticks, submissionNumber);
		
		String s = Tools.getElementTextContent(sw.waitForElement("#tab-t0-3 ion-badge"));
		int spinChances = Integer.parseInt(s);
		int oSpinChances = Integer.parseInt(ls.getSpinChances());
		if (submissionNumber == 2) {
			Assert.assertEquals(spinChances - oSpinChances, 2);
		} else {
			Assert.assertEquals(spinChances - oSpinChances, 1);
		}
		ls.addSpinChances(s);
		
		TestLogger.trace(String.format("completed assignment %s", i));
		return i;
	}

	@Override
	protected void studentSubmitAssessment() {
		super.studentSubmitAssessment();
		WebElement popup = null;// the button sometimes cover by another element, we just need to wait for the element to disappear
		int i = 5;
		while (i > 0) {
			try {
				popup = UIAction.waitForElementVisible(sw, "ion-content.items-popup ion-list.items-popup-data ion-grid ion-row ion-col");
				if (popup != null) {
					popup.findElement(Tools.getBy(ElementType.TAGNAME, "button")).click();
					Tools.forceToWait(BuildConfig.jsWaitTime);
					i = -1;
				}
			} catch (Exception e) {
				TestLogger.error(Throwables.getStackTraceAsString(e));
				i--;
				Tools.forceToWait(1);
			}
		}
		waitForLoadFinished();
		Tools.forceToWait(BuildConfig.jsWaitTime);
	}

	private void checkTicks(List<WebElement> ticks, int submissionNumber) {
		if (submissionNumber == 1) {// one submission should be one tick
			Assert.assertTrue(ticks.get(0).getAttribute("class").contains("fa-check-circle-o"));
			Assert.assertFalse(ticks.get(1).getAttribute("class").contains("fa-check-circle-o"));
		} else if (submissionNumber > 1) {// two submissions should be two ticks
			Assert.assertTrue(ticks.get(0).getAttribute("class").contains("fa-check-circle-o"));
			Assert.assertTrue(ticks.get(1).getAttribute("class").contains("fa-check-circle-o"));
		}
	}
	
}
