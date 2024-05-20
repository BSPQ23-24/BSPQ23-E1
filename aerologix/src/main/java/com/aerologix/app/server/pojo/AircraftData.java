package com.aerologix.app.server.pojo;
/**
 * A data transfer object (DTO) representing an aircraft.
 * <p>
 * This class is used to encapsulate aircraft information and is utilized for communication between different layers of the application.
 */
public class AircraftData {
    /** The unique identifier of the aircraft. */
    protected int id;
    /** The manufacturer of the aircraft. */
    protected String  manufacturer;
    /** The type or model of the aircraft. */
    protected String type ;
    /** The maximum capacity of the aircraft. */
    protected int maxCapacity;

    
    /**
     * Default constructor.
     * <p>
     * This constructor is required for serialization of the object.
     */
    public AircraftData() {
        
    }
    /**
     * Gets the unique identifier of the aircraft.
     *
     * @return the aircraft ID.
     */
    public int getId() {
	   
        return id;
    }
    /**
     * Sets the unique identifier of the aircraft.
     *
     * @param id the aircraft ID.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Gets the manufacturer of the aircraft.
     *
     * @return the manufacturer.
     */
    public String getManufacturer() {
		
        return manufacturer;
    }
	
    /**
     * Sets the manufacturer of the aircraft.
     *
     * @param manufacturer the manufacturer.
     */
    public void setManufacturer(String manufacturer ) {
	
        this.manufacturer = manufacturer;
    }

    /**
     * Gets the type or model of the aircraft.
     *
     * @return the type.
     */
   public String getType() {
	
        return type;
    }
   /**
    * Sets the type or model of the aircraft.
    *
    * @param type the type.
    */
    public void setType(String  type) {
		
        this.type = type;
    }
    /**
     * Gets the maximum capacity of the aircraft.
     *
     * @return the maximum capacity.
     */
    public int getMaxCapacity() {
		
        return maxCapacity;
    }
    /**
     * Sets the maximum capacity of the aircraft.
     *
     * @param maxCapacity the maximum capacity.
     */
    public void setMaxCapacity(int maxCapacity) {
	
        this.maxCapacity = maxCapacity;
    }
    /**
     * Returns a string representation of the aircraft data.
     *
     * @return a string representing the aircraft.
     */
    public String toString() {
        return "Aircraft{" +  "manufacturer='" +  manufacturer + '\'' +  ", type='" + type + '\'' + ", maxCapacity=" + maxCapacity +   '}';
    }

}
