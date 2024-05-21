package com.aerologix.app.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
/**
 * @brief Represents a airline entity in the system.
 * 
 * <p>
 * This class is used for persistence with JDO (Java Data Objects). It includes
 * details about an airline such as its name. The class provides getter and setter 
 * methods to access and modify the fields.
 */
@PersistenceCapable
public class Airline {
	/** The unique identifier for the airline, generated incrementally. */
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
	protected int idAirline;
    /** The name of the airline. */
	protected String name;
	
	/**
     * Default constructor initializing fields to default values.
     */
	public Airline() {
        this.idAirline = -1;
        this.name = "";
    }
	   /**
     * Parameterized constructor to initialize an airline with the specified values.
     *
     * @param idAirline The unique identifier for the airline.
     * @param name The name of the airline.
     */
    public Airline(int idAirline, String name) {
        this.setId(idAirline);
        this.setName(name);
    }

    /**
     * Gets the unique identifier for the airline.
     *
     * @return The ID of the airline.
     */
    public int getId() {
        return this.idAirline;
    }
    /**
     * Sets the unique identifier for the airline.
     *
     * @param idAirline The new ID of the airline.
     */
    public void setId(int idAirline) {
        this.idAirline = idAirline;
    }
    /**
     * Gets the name of the airline.
     *
     * @return The name of the airline.
     */
    public String getName() {
        return this.name;
    }
    /**
     * Sets the name of the airline.
     *
     * @param name The new name of the airline.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns a string representation of the airline.
     *
     * @return A string representing the airline.
     */
    public String toString() {
        return "Airline"+this.name+ "id:"+this.idAirline;
    }

	
}


