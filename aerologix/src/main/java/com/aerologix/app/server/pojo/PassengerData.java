package com.aerologix.app.server.pojo;
/**
 * A data transfer object (DTO) representing the passenger data.
 * <p>
 * This class is used to encapsulate passenger information and is utilized for communication between different layers of the application.
 */
public class PassengerData {
	/** The unique identifier of the passenger(DNI) */
    protected String dni;
    /** The contact number of the passenger*/
	protected int phone;
	/** The name of the passenger*/
	protected String name;
	/** The contact email of the passenger*/
	protected String email;
	/** The nationality of the passenger*/
	protected String nationality;
	/** A long that represents the birth date of the passenger*/
	protected long birthdate;
    /**
     * Default constructor.
     * <p>
     * This constructor is required for serialization of the object.
     */
    public PassengerData() {
        
    }
    /**
     * Gets the DNI of the passenger that works as a unique ID.
     *
     * @return the ID of the passenger.
     */
    public String getDNI() {
        return this.dni;
    }
    /**
     * Gets the contact number of the passenger of the passenger.
     *
     * @return the phone number of the passenger.
     */
    public int getPhone() {
        return this.phone;
    }
    /**
     * Gets the name of the passenger.
     *
     * @return the name of the passenger.
     */
    public String getName() {
        return this.name;
    }
    /**
     * Gets the contact email of the passenger.
     *
     * @return the contact email of the passenger.
     */
    public String getEmail() {
        return this.email;
    }
    /**
     * Gets the nationality of the passenger.
     *
     * @return the nationality of the passenger.
     */
    public String getNationality() {
        return this.nationality;
    }
    /**
     * Gets the birth date of the passenger.
     *
     * @return the birth date of the passenger.
     */
    public long getBirthdate() {
        return this.birthdate;
    }

    /**
     * Sets the ID of the passenger.
     *
     * @param dni the ID of the passenger.
     */
    public void setDNI(String dni) {
        this.dni = dni;
    }
    /**
     * Sets the contact phone of the passenger.
     *
     * @param phone the phone number of the passenger.
     */
    public void setPhone(int phone) {
        this.phone = phone;
    }
    /**
     * Sets the name of the passenger.
     *
     * @param name the name of the passenger.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Sets the contact email of the passenger.
     *
     * @param email the email of the passenger.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Sets the nationality of the passenger.
     *
     * @param nationality the nationality of the passenger.
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    /**
     * Sets the birth date of the passenger.
     *
     * @param birth date long that represents the birth date of the passenger.
     */
    public void setBirthdate(long birthdate) {
        this.birthdate = birthdate;
    }

	  /**
     * Returns a string representation of the passenger data.
     *
     * @return a string representing the passenger data.
     */
    public String toString() {
        return "Passenger{" +
                "dni='" + dni + '\'' +
                ", phone=" + phone +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", nationality='" + nationality + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }
    
}
