package model.jobsmart;


import java.util.ArrayList;
import java.util.Objects;


public class ActivitySequence {

	private String name;
	private String description;
	private ArrayList<JobSmartAssessment> assessments;
	
	public ActivitySequence(String name, String description, ArrayList<JobSmartAssessment> assessments) {
		super();
		this.name = name;
		this.description = description;
		this.assessments = assessments;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public ArrayList<JobSmartAssessment> getAssessments() {
		return assessments;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, assessments);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActivitySequence other = (ActivitySequence) obj;
		if (assessments == null) {
			if (other.assessments != null)
				return false;
		} else if (!assessments.equals(other.assessments))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
