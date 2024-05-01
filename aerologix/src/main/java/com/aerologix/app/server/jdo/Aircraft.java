package com.aerologix.app.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.IdGeneratorStrategy;

@PersistenceCapable(detachable = "true")
public class Aircraft {
	
    protected String  manufacturer;
    protected String type ;
    protected int maxCapacity;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
	protected int id;

    public Aircraft() {
        this.id = -1;
        this.manufacturer = null;
        this.type = null;
        this.maxCapacity = -1;
    }

    public Aircraft(int id,  String manufacturer, String type,  int maxCapacity ) {
        this.id = id;
        this.manufacturer = manufacturer;   
        this.type = type;
        this.maxCapacity = maxCapacity;
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
