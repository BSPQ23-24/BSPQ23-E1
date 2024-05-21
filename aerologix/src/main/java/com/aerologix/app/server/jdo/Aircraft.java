package com.aerologix.app.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.IdGeneratorStrategy;
/**
 * @brief Represents a aircraft entity in the system.
 * 
 * <p>
 * This class is used for persistence with JDO (Java Data Objects). It includes
 * details about an aircraft such as its manufacturer, type, and maximum capacity.
 * The class provides getter and setter methods to access and modify the fields.
 */
@PersistenceCapable(detachable = "true")
public class Aircraft {
	/** The manufacturer of the aircraft. */
    protected String  manufacturer;
    /** The type of the aircraft. */
    protected String type ;
    /** The maximum capacity of the aircraft. */
    protected int maxCapacity;
    /** The unique identifier for the aircraft, generated incrementally. */
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
	protected int id;
    /**
     * Default constructor initializing fields to default values.
     */
    public Aircraft() {
        this.id = -1;
        this.manufacturer = null;
        this.type = null;
        this.maxCapacity = -1;
    }
    /**
     * Parameterized constructor to initialize an aircraft with the specified values.
     *
     * @param id The unique identifier for the aircraft.
     * @param manufacturer The manufacturer of the aircraft.
     * @param type The type of the aircraft.
     * @param maxCapacity The maximum capacity of the aircraft.
     */
    public Aircraft(int id,  String manufacturer, String type,  int maxCapacity ) {
        this.id = id;
        this.manufacturer = manufacturer;   
        this.type = type;
        this.maxCapacity = maxCapacity;
    }

    /**
     * Gets the unique identifier for the aircraft.
     *
     * @return The ID of the aircraft.
     */
   public int getId() {
	   
        return id;
    }
   /**
    * Sets the unique identifier for the aircraft.
    *
    * @param id The new ID of the aircraft.
    */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Gets the manufacturer of the aircraft.
     *
     * @return The manufacturer of the aircraft.
     */
    public String getManufacturer() {
		
        return manufacturer;
    }
	
    /**
     * Sets the manufacturer of the aircraft.
     *
     * @param manufacturer The new manufacturer of the aircraft.
     */
    public void setManufacturer(String manufacturer ) {
	
        this.manufacturer = manufacturer;
    }

    /**
     * Gets the type of the aircraft.
     *
     * @return The type of the aircraft.
     */
   public String getType() {
	
        return type;
    }
   /**
    * Sets the type of the aircraft.
    *
    * @param type The new type of the aircraft.
    */
    public void setType(String  type) {
		
        this.type = type;
    }
    /**
     * Gets the maximum capacity of the aircraft.
     *
     * @return The maximum capacity of the aircraft.
     */
    public int getMaxCapacity() {
		
        return maxCapacity;
    }
    /**
     * Sets the maximum capacity of the aircraft.
     *
     * @param maxCapacity The new maximum capacity of the aircraft.
     */
    public void setMaxCapacity(int maxCapacity) {
	
        this.maxCapacity = maxCapacity;
    }
    /**
     * Returns a string representation of the aircraft.
     *
     * @return A string representing the aircraft.
     */
    public String toString() {
        return "Aircraft{" +  "manufacturer='" +  manufacturer + '\'' +  ", type='" + type + '\'' + ", maxCapacity=" + maxCapacity +   '}';
    }
}
