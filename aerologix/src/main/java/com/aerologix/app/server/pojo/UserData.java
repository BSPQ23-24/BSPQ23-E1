package com.aerologix.app.server.pojo;
/**
 * @brief A data transfer object (DTO) representing a user data.
 * 
 * <p>
 * This class is used to encapsulate user information and is utilized for communication between different layers of the application.
 */
public class UserData {
	/** The unique identifier of the user(email) */
    protected String email = "";
    /** The password of the user*/
    protected String password = "";
    /** The type of user */
    public enum UserType {
    	COUNTER_CLERK, ADMIN
    }
    /** The type of user from the enum {@link UserType} */
    protected String userType = "";
    /** The name of the user*/
    protected String name = "";
    /**
     * Default constructor.
     * <p>
     * This constructor is required for serialization of the object.
     */
    public UserData() {
       
    }
    /**
     * Gets the email of the user that performs as a unique ID.
     *
     * @return the email of the user.
     */
    public String getEmail() {
		return this.email;
	}
    /**
     * Sets the email of the user.
     *
     * @param email the email of the user.
     */
	public void setEmail(String email) {
		this.email = email;
	}
	  /**
     * Gets the password from the user.
     *
     * @return the password of the user.
     */
	public String getPassword() {
		return this.password;
	}
    /**
     * Sets the password of the user.
     *
     * @param password the password of the user.
     */
	public void setPassword(String password) {
		this.password = password;
	}
	  /**
     * Gets the type of the user.
     *
     * @return the type of the user.
     */
	public String getUserType() {
		return this.userType;
	}
	 /**
     * Sets the type of the user.
     *
     * @param type the type of the user.
     */
	public void setUserType(String userType) {
		this.userType = UserType.valueOf(userType).toString();
	}
	  /**
     * Gets the name of the user.
     *
     * @return the name of the user.
     */
	public String getName() {
		return this.name;
	}
	 /**
     * Sets the name of the user.
     *
     * @param name the name of the user.
     */
	public void setName(String name) {
		this.name = name;
	}
	  /**
     * Returns a string representation of the login data.
     *
     * @return a string representing the user.
     */
    public String toString() {
		return "User [email=" + email + ", password=" + password + ", userType=" + userType + ", name=" + name + "]";
	}
}
