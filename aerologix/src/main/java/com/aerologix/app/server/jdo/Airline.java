package com.aerologix.app.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Airline {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
	protected int idAirline;
	protected String name;
	
	public Airline() {
        this.idAirline = -1;
        this.name = "";
    }

    public Airline(int idAirline, String name) {
        this.setId(idAirline);
        this.setName(name);
    }

    // Getters and setters
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


