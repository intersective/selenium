package model;

import java.util.Objects;


public class MentorAnswer {

	private String type;
	private String answer;
	
	public MentorAnswer(String type, String answer) {
		super();
		this.type = type;
		this.answer = answer;
	}

	public String getType() {
		return type;
	}

	public String getAnswer() {
		return answer;
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, answer);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MentorAnswer other = (MentorAnswer) obj;
		if (answer == null) {
			if (other.answer != null)
				return false;
		} else if (!answer.equals(other.answer))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
}
