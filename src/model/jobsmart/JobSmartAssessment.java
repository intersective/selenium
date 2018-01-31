package model.jobsmart;


import java.util.ArrayList;
import java.util.Objects;

import model.MentorQuestion;
import model.Question;


public class JobSmartAssessment {

	private String name;
	private String practeraName;
	private String description;
	private boolean publishScore;
	private ArrayList<Question> questions;
	private ArrayList<MentorQuestion> mentorQuestions;
	
	public JobSmartAssessment(String name, String practeraName, String description, boolean publishScore,
			ArrayList<Question> questions, ArrayList<MentorQuestion> mentorQuestions) {
		super();
		this.name = name;
		this.practeraName = practeraName;
		this.description = description;
		this.publishScore = publishScore;
		this.questions = questions;
		this.mentorQuestions = mentorQuestions;
	}

	public String getName() {
		return name;
	}

	public String getPracteraName() {
		return practeraName;
	}

	public String getDescription() {
		return description;
	}

	public boolean isPublishScore() {
		return publishScore;
	}

	public ArrayList<Question> getQuestions() {
		return questions;
	}

	public ArrayList<MentorQuestion> getMentorQuestions() {
		return mentorQuestions;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, practeraName, description, publishScore, questions, mentorQuestions);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobSmartAssessment other = (JobSmartAssessment) obj;
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
		if (practeraName == null) {
			if (other.practeraName != null)
				return false;
		} else if (!practeraName.equals(other.practeraName))
			return false;
		if (publishScore != other.publishScore)
			return false;
		if (questions == null) {
			if (other.questions != null)
				return false;
		} else if (!questions.equals(other.questions))
			return false;
		if (mentorQuestions == null) {
			if (other.mentorQuestions != null)
				return false;
		} else if (!mentorQuestions.equals(other.mentorQuestions))
			return false;
		return true;
	}

}
