package model;


import java.util.Objects;


public class Activity {

	private String title;
	private String ticks;
	private String score;

	public Activity(String title, String ticks, String score) {
		super();
		this.title = title;
		this.ticks = ticks;
		this.score = score;
	}

	public String getTitle() {
		return title;
	}

	public String getTicks() {
		return ticks;
	}

	public String getScore() {
		return score;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Activity another = (Activity) obj;
		return Objects.equals(title, another.getTitle()) && Objects.equals(ticks, another.getTicks()) && Objects.equals(score, another.getScore());
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, ticks, score);
	}
	
}
