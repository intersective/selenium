package model;


public class Event {

	private String id;
	private String name;
	private String isOriginal;
	
	public Event(String id, String name, String isoriginal) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIsOriginal() {
		return isOriginal;
	}
	
}
