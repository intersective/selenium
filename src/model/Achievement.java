package model;


import java.util.ArrayList;
import java.util.Objects;


public class Achievement {

	private ArrayList<String> achievements;

	public Achievement(ArrayList<String> achievements) {
		super();
		this.achievements = achievements;
	}

	public ArrayList<String> getAchievements() {
		return achievements;
	}

	public void setAchievements(ArrayList<String> achievements) {
		this.achievements = achievements;
	}

	@Override
	public int hashCode() {
		return Objects.hash(achievements);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Achievement other = (Achievement) obj;
		if (achievements == null) {
			if (other.achievements != null) {
				return false;
			}
		} else if (!achievements.equals(other.achievements)) {
			return false;
		}
		return true;
	}
	
}
