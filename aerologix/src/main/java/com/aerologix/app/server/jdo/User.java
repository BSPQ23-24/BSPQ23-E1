package com.aerologix.app.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class User {
	
	// Attributes
	@PrimaryKey
	protected String email;
    protected String password;
    public enum UserType {
    	COUNTER_CLERK, ADMIN
    }
    protected UserType userType;
    protected String name;
    
    // Constructors
    public User() {
		this.email = "";
		this.password = "";
		this.userType = null;
		this.name = "";
	}
     
	public User(String email, String password, UserType userType, String name) {
		this.setEmail(email);
		this.setPassword(password);
		this.setUserType(userType);
		this.setName(name);
	}

	// Getters and setters  
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		return "User [email=" + email + ", password=" + password + ", userType=" + userType + ", name=" + name + "]";
	}
}