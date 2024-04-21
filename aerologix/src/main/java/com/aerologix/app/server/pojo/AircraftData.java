package com.aerologix.app.server.pojo;

public class AircraftData {

    protected int id;
    protected String  manufacturer;
    protected String type ;
    protected int maxCapacity;

    public AircraftData() {
        // required by serialization
    }

    public int getId() {
	   
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManufacturer() {
		
        return manufacturer;
    }
	

    public void setManufacturer(String manufacturer ) {
	
        this.manufacturer = manufacturer;
    }


   public String getType() {
	
        return type;
    }

    public void setType(String  type) {
		
        this.type = type;
    }

    public int getMaxCapacity() {
		
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
	
        this.maxCapacity = maxCapacity;
    }

    public String toString() {
        return "Aircraft{" +  "manufacturer='" +  manufacturer + '\'' +  ", type='" + type + '\'' + ", maxCapacity=" + maxCapacity +   '}';
    }

}
