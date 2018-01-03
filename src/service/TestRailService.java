package service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import service.testrail.APIClient;
import service.testrail.APIException;

import com.google.common.base.Throwables;
import common.BuildConfig;


public class TestRailService {

	private APIClient client;
	
	private static class TestRailServiceHolder {
		private static final TestRailService my = new TestRailService();
	}
	
	public static TestRailService getInstance() {
		return TestRailServiceHolder.my;
	}
	
	private TestRailService() {
		client = new APIClient(BuildConfig.testrailUrl);
		client.setUser(BuildConfig.testrailUser);
		client.setPassword(BuildConfig.testrailPassword);
	}
	
	public JSONObject addCaseResultWithStep(String runId, String caseId, Map<String, Object> data) {
		try {
			return (JSONObject) client.sendPost(String.format("add_result_for_case/%s/%s", runId, caseId), data);
		} catch (IOException | APIException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
		return null;
	}
	
	public JSONObject getCase(String caseId) {
		try {
			return (JSONObject) client.sendGet(String.format("get_case/%s", caseId));
		} catch (IOException | APIException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
		return null;
	}
	
	public JSONObject addTestRun(String projectId, String assignedtoId, ArrayList<String> cases) {
		try {
			Calendar current = Calendar.getInstance();
			String currentTimeStr = String.format("%s/%s/%s-%s-%s", current.get(Calendar.DAY_OF_MONTH), current.get(Calendar.MONTH) + 1, current.get(Calendar.YEAR), 
					current.get(Calendar.HOUR_OF_DAY), current.get(Calendar.MINUTE));
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("name", String.format("PE test %s", currentTimeStr));
			data.put("description", "This is a new test run");
			data.put("assignedto_id", assignedtoId);
			if (cases != null) {
				data.put("case_ids", cases);
				data.put("include_all", false);
			}
			return (JSONObject) client.sendPost(String.format("add_run/%s", projectId), data);
		} catch (IOException | APIException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
		return null;
	}
	
	public JSONArray getCaeseForProject(String projectId) {
		try {
			return (JSONArray) client.sendGet(String.format("get_cases/%s", projectId));
		} catch (IOException | APIException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
		return null;
	}
	
}
