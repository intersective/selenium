package model;


import java.util.ArrayList;
import java.util.Objects;


public class Assessment {

	private String name;
	private String description;
	private ArrayList<Question> questions;
	
	public Assessment(String name, String description) {
		super();
		this.name = name;
		this.description = description;
		this.questions = new ArrayList<Question>();
	}
	
	public void addQuestion(Question question){
		this.questions.add(question);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Question getQuestion(int index) {
		return questions.get(index);
	}
	
	public int getNumberOfQuestions() {
		return questions == null ? 0 : questions.size();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Assessment another = (Assessment) obj;
		if (!Objects.equals(name, another.getName()) || !Objects.equals(description, another.getDescription())) {
			return false;
		}
		int total = getNumberOfQuestions();
		int totalan = another.getNumberOfQuestions();
		if (total != totalan) {
			return false;
		}
		for (int i = 0; i < total; i++) {
			if (!Objects.equals(questions.get(i), another.getQuestion(i))) {
				System.out.println(String.format("%s %s", questions.get(i).getQcontent(), another.getQuestion(i).getQcontent()));
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, questions);
	}
	
}
