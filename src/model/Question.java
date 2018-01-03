package model;


import java.util.Objects;


public class Question {

	private String qcontent;
	private String description;
	private String qtype;
	private String answer;
	private String failAnswer;
	
	public Question(String qcontent, String description, String qtype, String answer, String failAnswer) {
		super();
		this.qcontent = qcontent;
		this.description = description;
		this.qtype = qtype;
		this.answer = answer;
		this.failAnswer = failAnswer;
	}

	public String getQcontent() {
		return qcontent;
	}

	public String getQtype() {
		return qtype;
	}

	public String getAnswer() {
		return answer;
	}

	public String getFailAnswer() {
		return failAnswer;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Question another = (Question) obj;
		return Objects.equals(qcontent, another.getQcontent())
				&& Objects.equals(description, another.getDescription())
				&& Objects.equals(qtype, another.getQtype())
				&& Objects.equals(answer, another.getAnswer())
				&& Objects.equals(failAnswer, another.getFailAnswer());
	}

	@Override
	public int hashCode() {
		return Objects.hash(qcontent, description, qtype, answer, failAnswer);
	}
	
}
