package com.aerologix.serialization;

public class Aircraft {
	
    protected String  manufacturer;
    protected String type ;
    protected int maxCapacity;


   
    public Aircraft(String manufacturer, String type, int maxCapacity) {
	
        this.manufacturer = manufacturer;   
        this.type = type;
        this.maxCapacity = maxCapacity;
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

 
    @Override
    public String toString() {
        return "Aircraft{" +  "manufacturer='" +  manufacturer + '\'' +  ", type='" + type + '\'' + ", maxCapacity=" + maxCapacity +   '}';
    }
}
