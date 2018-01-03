package service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import service.TestLogger;
import service.TestRailService;

import com.google.common.base.Throwables;


public class AddCaseResultTask implements Runnable {

	private String methodName;
	private Throwable t;
	private long duration;
	private String runCaseId;
	private String runId;
	
	public AddCaseResultTask(String methodName, Throwable t, long duration,
			String runCaseId, String runId) {
		super();
		this.methodName = methodName;
		this.t = t;
		this.duration = duration;
		this.runCaseId = runCaseId;
		this.runId = runId;
	}

	@Override
	public void run() {
		String content;
		Integer statusId = t !=null ? new Integer(5) : new Integer(1);
		if ("main".equals(methodName)) {
			content = "Step 1";
		} else {
			content = String.format("Step %s", methodName.substring(4));
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("status_id", statusId);
		data.put("comment", t == null ? "works fine so far" : Throwables.getStackTraceAsString(t));
		data.put("elapsed", String.format("%ss", String.valueOf(new Long(duration < 1 ? 1 : duration).intValue())));
		ArrayList<HashMap<String, Object>> stepResult = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> oneStep = new HashMap<String, Object>();
		oneStep.put("status_id", statusId);
		oneStep.put("content", content);
		oneStep.put("expected", "complete");
		oneStep.put("actual", t == null ? "complete" : "incomplete");
		stepResult.add(oneStep);
		data.put("custom_step_results", stepResult);
		
		JSONObject tResult = TestRailService.getInstance().addCaseResultWithStep(runId, runCaseId, data);
		if (tResult !=null) {
			TestLogger.trace(tResult.toJSONString());
		}
	}

}
