package model;


public class User {

	private String userName;
	private String regurl;
	
	public User(String userName, String regurl) {
		super();
		this.userName = userName;
		this.regurl = regurl;
	}

	public String getUserName() {
		return userName;
	}

	public String getRegurl() {
		return regurl;
	}
	
}
