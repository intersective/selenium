package model;


public class Topic {

	private String title;
	private String description;
	
	public Topic(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
	
}
