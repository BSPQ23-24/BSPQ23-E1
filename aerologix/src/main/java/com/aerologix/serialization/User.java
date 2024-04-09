package com.aerologix.serialization;

public class User {
	
	// Attributes
    protected String userId;
    protected String password;
    enum UserType {
    	COUNTER_CLERK, ADMIN
    }
    protected UserType userType;
    protected String name;
    
    // Constructors
    public User() {
		this.userId = "";
		this.password = "";
		this.userType = null;
		this.name = "";
	}
     
	public User(String userId, String password, UserType userType, String name) {
		this.setUserId(userId);
		this.setPassword(password);
		this.setUserType(userType);
		this.setName(name);
	}

	// Getters and setters  
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserType getUserType() {
		return this.userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Print the instance
	public String toString() {
		return "User [userId=" + userId + ", password=" + password + ", userType=" + userType + ", name=" + name + "]";
	}
}