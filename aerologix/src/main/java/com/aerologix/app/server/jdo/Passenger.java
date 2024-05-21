package com.aerologix.app.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/**
 * @brief Represents a passenger entity in the system.
 * <p>
 * This class is annotated for persistence with JDO (Java Data Objects). It includes
 * details about a passenger such as their DNI, phone number, name, email, nationality, and birthdate.
 * The class provides getter and setter methods to access and modify the fields.
 */
@PersistenceCapable(detachable = "true")
public class Passenger {
	
	/** The unique identifier (DNI) for the passenger. */
    @PrimaryKey
    protected String dni;
    /** The phone number of the passenger. */
	protected int phone;
	/** The name of the passenger. */
	protected String name;
	/** The email address of the passenger. */
	protected String email;
	/** The nationality of the passenger. */
	protected String nationality;
	 /** The birthdate of the passenger as a long value. */
	protected long birthdate;
    /**
     * Constructs a new Passenger instance with the specified details.
     *
     * @param dni The unique identifier (DNI) for the passenger.
     * @param phone The phone number of the passenger.
     * @param name The name of the passenger.
     * @param email The email address of the passenger.
     * @param nationality The nationality of the passenger.
     * @param birthdate The birthdate of the passenger.
     */
    public Passenger( String dni, int phone, String name, String email, String nationality, long birthdate) {
        this.dni = dni;
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.nationality = nationality;
        this.birthdate = birthdate;
    }

    /**
     * Gets the unique identifier (DNI) for the passenger.
     *
     * @return The DNI of the passenger.
     */
    public String getDNI() {
        return this.dni;
    }
    /**
     * Gets the phone number of the passenger.
     *
     * @return The phone number of the passenger.
     */
    public int getPhone() {
        return this.phone;
    }
    /**
     * Gets the name of the passenger.
     *
     * @return The name of the passenger.
     */
    public String getName() {
        return this.name;
    }
    /**
     * Gets the email address of the passenger.
     *
     * @return The email address of the passenger.
     */
    public String getEmail() {
        return this.email;
    }
    /**
     * Gets the nationality of the passenger.
     *
     * @return The nationality of the passenger.
     */
    public String getNationality() {
        return this.nationality;
    }
    /**
     * Gets the birthdate of the passenger.
     *
     * @return The birthdate of the passenger.
     */
    public long getBirthdate() {
        return this.birthdate;
    }

    /**
     * Sets the unique identifier (DNI) for the passenger.
     *
     * @param dni The new DNI of the passenger.
     */
    public void setDNI(String dni) {
        this.dni = dni;
    }
    /**
     * Sets the phone number of the passenger.
     *
     * @param phone The new phone number of the passenger.
     */
    public void setPhone(int phone) {
        this.phone = phone;
    }
    /**
     * Sets the name of the passenger.
     *
     * @param name The new name of the passenger.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Sets the email address of the passenger.
     *
     * @param email The new email address of the passenger.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Sets the nationality of the passenger.
     *
     * @param nationality The new nationality of the passenger.
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    /**
     * Sets the birth date of the passenger.
     *
     * @param birthdate The new birth date of the passenger.
     */
    public void setBirthdate(long birthdate) {
        this.birthdate = birthdate;
    }

    /**
     * Returns a string representation of the passenger.
     *
     * @return A string representing the passenger.
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
