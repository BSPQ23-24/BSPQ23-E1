package com.aerologix.app.server.pojo;
/**
 * @brief A data transfer object (DTO) representing a airline.
 * 
 * <p>
 * This class is used to encapsulate airline information and is utilized for communication between different layers of the application.
 */
public class AirlineData {
	/** The unique identifier of the airline */
    protected int idAirline;
    /** The name of the airline */
	protected String name;
	/**
     * Default constructor.
     * <p>
     * This constructor is required for serialization of the object.
     */
    public AirlineData() {
        
    }
    /**
     * Gets the unique identifier of the airline.
     *
     * @return the airline ID.
     */
    public int getId() {
        return this.idAirline;
    }
    /**
     * Sets the unique identifier of the airline.
     *
     * @param idAirline the airline ID.
     */
    public void setId(int idAirline) {
        this.idAirline = idAirline;
    }
    /**
     * Gets the name of the airline.
     *
     * @return the airline name.
     */
    public String getName() {
        return this.name;
    }
    /**
     * Sets the name of the airline.
     *
     * @param name the airline name.
     */
    public void setName(String name) {
        this.name = name;
    }
    
	  /**
     * Returns a string representation of the airline data.
     *
     * @return a string representing the airline.
     */
    public String toString() {
        return "Airline"+this.name+ "id:"+this.idAirline;
    }

}
