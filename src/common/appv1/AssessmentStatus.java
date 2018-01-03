package common.appv1;


public enum AssessmentStatus {

	DONE("done"), PUBLISHED("published"), PENDINGREVIEW("pending review"), PENDINGAPPROVAL("pending approval");
	
	private final String text;
	
	private AssessmentStatus(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
