package com.aerologix.app.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
/**
 * @brief Represents a user in the system.
 * <p>
 * This class is annotated for persistence with JDO (Java Data Objects). It includes details about a user
 * such as their email, password, user type, and name. The class provides getter and setter methods to access
 * and modify the fields.
 */
@PersistenceCapable
public class User {
	
	/** The email address of the user, used as the primary key. */
	@PrimaryKey
	protected String email;
	 /** The password of the user. */
    protected String password;
    /** The type of user, either COUNTER_CLERK or ADMIN. */
    public enum UserType {
    	COUNTER_CLERK, ADMIN
    }
    /** The name of the user. */
    protected UserType userType;
    protected String name;
    
    /**
     * Default constructor. Initializes the user with default values.
     */
    public User() {
		this.email = "";
		this.password = "";
		this.userType = null;
		this.name = "";
	}
    /**
     * Constructs a new User instance with the specified details.
     *
     * @param email The email address of the user.
     * @param password The password of the user.
     * @param userType The type of user.
     * @param name The name of the user.
     */
	public User(String email, String password, UserType userType, String name) {
		this.setEmail(email);
		this.setPassword(password);
		this.setUserType(userType);
		this.setName(name);
	}

    /**
     * Gets the email address of the user.
     *
     * @return The email address of the user.
     */ 
	public String getEmail() {
		return this.email;
	}
    /**
     * Sets the email address of the user.
     *
     * @param email The new email address of the user.
     */
	public void setEmail(String email) {
		this.email = email;
	}
    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
	public String getPassword() {
		return this.password;
	}
    /**
     * Sets the password of the user.
     *
     * @param password The new password of the user.
     */
	public void setPassword(String password) {
		this.password = password;
	}
    /**
     * Gets the type of user.
     *
     * @return The user type.
     */
	public UserType getUserType() {
		return this.userType;
	}
    /**
     * Sets the type of user.
     *
     * @param userType The new user type.
     */
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
    /**
     * Gets the name of the user.
     *
     * @return The name of the user.
     */
	public String getName() {
		return this.name;
	}
    /**
     * Sets the name of the user.
     *
     * @param name The new name of the user.
     */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * Returns a string representation of the user.
     *
     * @return A string representing the user.
     */
	public String toString() {
		return "User [email=" + email + ", password=" + password + ", userType=" + userType + ", name=" + name + "]";
	}
}