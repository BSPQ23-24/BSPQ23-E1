package com.aerologix.app.server.pojo;
/**
	 * A data transfer object (DTO) representing a login session.
	 * <p>
	 * This class is used to encapsulate login attempt information and is utilized for communication between different layers of the application.
	 */
public class LoginData {
	/** The unique identifier of the login(user email) */
    private String email;
    /** The password introduced to perform the login*/
    private String password;
    /**
     * Default constructor.
     * <p>
     * This constructor is required for serialization of the object.
     */
    public LoginData() {
        
    }
    /**
     * Gets the email introduced to perform the login attempt.
     *
     * @return the email of the login attempt.
     */
    public String getEmail() {
        return this.email;
    }
    /**
     * Sets the email of the login attempt.
     *
     * @param email the email wanted to perform the login attempt.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Gets the password introduced to perform the login attempt.
     *
     * @return the password of the login attempt.
     */
    public String getPassword() {
        return this.password;
    }
    /**
     * Sets the password of the login attempt.
     *
     * @param password the password wanted to perform the login attempt.
     */
    public void setPassword(String password) {
        this.password = password;
    }
	  /**
     * Returns a string of the login data.
     *
     * @return a string representing the login attempt.
     */
    public String toString() {
        return "Login [email=" + email + ", password=" + password + "]";
    }

}
