package com.aerologix.app.server.pojo;

public class AirlineData {
    protected int idAirline;
	protected String name;

    public AirlineData() {
        // required by serialization
    }

    public int getId() {
        return this.idAirline;
    }

    public void setId(int idAirline) {
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
