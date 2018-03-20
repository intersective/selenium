package testsuit.practera;


import java.util.ArrayList;

import model.Questionnaire;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.AssignmentDataService;

import common.BuildConfig;


public class TestAppv1PublishReview extends TestPublishReview {

	@BeforeClass
	public void setup() {
		super.setup();
		setUserName(BuildConfig.appv1UserName.split("@")[0]);
		setLoginUserName(BuildConfig.appv1AdminUser);
		setLoginPassword(BuildConfig.appv1AdminPassword);
		setname("test publish reviews for student submissions for app v1");
	}
	
	@Test(description = "test publish reviews for student submissions", groups = "practera_review_publish_appv1")
	public void main() {
		super.main();
	}

	@Override
	protected void initAssessmentData() {
		assessments = new ArrayList<String>();
		ArrayList<Questionnaire> qns = AssignmentDataService.getInstance().loadListDataFromJsonFiles("assessments_appv1student", 1, Questionnaire.class);
		assessments.add(qns.get(0).getAssessment(2).getName());
	}

	@Override
	protected void logout() {
		actions.logout(sw, "3");
	}
	
}
