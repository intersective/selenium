package model;

import java.util.ArrayList;
import java.util.Objects;


public class MentorQuestion {

	private String name;
	private String description;
	private ArrayList<MentorAnswer> mentorAnswers;
	
	public MentorQuestion(String name, String description, ArrayList<MentorAnswer> mentorAnswers) {
		super();
		this.name = name;
		this.description = description;
		this.mentorAnswers = mentorAnswers;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public ArrayList<MentorAnswer> getMentorAnswers() {
		return mentorAnswers;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, mentorAnswers);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MentorQuestion other = (MentorQuestion) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (mentorAnswers == null) {
			if (other.mentorAnswers != null)
				return false;
		} else if (!mentorAnswers.equals(other.mentorAnswers))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
