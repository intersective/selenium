package model;


import java.util.ArrayList;
import java.util.Objects;


public class Questionnaire {
	
	private String name;
	private String description;
	private ArrayList<Assessment> assessments;
	
	public Questionnaire(String name, String description) {
		super();
		this.name = name;
		this.description = description;
		this.assessments = new ArrayList<Assessment>();
	}
	
	public void addAssessment(Assessment assessment) {
		this.assessments.add(assessment);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public Assessment getAssessment(int index) {
		return assessments.get(index);
	}
	
	public int getNumberOfAssessments() {
		return assessments == null ? 0 : assessments.size();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Questionnaire another = (Questionnaire) obj;
		if (!Objects.equals(name, another.getName()) || !Objects.equals(description, another.getDescription())) {
			return false;
		}
		int total = getNumberOfAssessments();
		int totalan = another.getNumberOfAssessments();
		if (total != totalan) {
			return false;
		}
		for (int i = 0; i < total; i++) {
			if (!Objects.equals(assessments.get(i), another.getAssessment(i))) {
				System.out.println(String.format("%s %s", assessments.get(i).getName(), another.getAssessment(i).getName()));
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, assessments);
	}
	
}
