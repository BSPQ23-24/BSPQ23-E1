package com.aerologix.serialization;

public class Airline {
	protected String idAirline;
	protected String name;
	
	public Airline() {
        this.idAirline = "";
        this.name = "";
    }

    public Airline(String idAirline, String name) {
        this.setId(idAirline);
        this.setName(name);
    }

    // Getters and setters
    public String getId() {
        return this.idAirline;
    }

    public void setId(String idAirline) {
        this.idAirline = idAirline;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    public String toString() {
        return "Airline"+this.name+ "id:"+this.idAirline;
    }

	
}


