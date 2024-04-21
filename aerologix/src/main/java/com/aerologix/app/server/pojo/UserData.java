package com.aerologix.app.server.pojo;

public class UserData {
    protected String email = "";
    protected String password = "";
    public enum UserType {
    	COUNTER_CLERK, ADMIN
    }
    protected String userType = "";
    protected String name = "";

    public UserData() {
        // required by serialization
    }

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

	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = UserType.valueOf(userType).toString();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String toString() {
		return "User [email=" + email + ", password=" + password + ", userType=" + userType + ", name=" + name + "]";
	}
}
