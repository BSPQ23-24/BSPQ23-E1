package com.aerologix.serialization;

import java.time.LocalDate;

public class Passenger {
	
    // Attributes
    protected String dni;
	protected int phone;
	protected String name;
	protected String email;
	protected String nationality;
	protected LocalDate birthdate; // he añadido este atributo


    public Passenger( String dni, int phone, String name, String email, String nationality, LocalDate birthdate) {
        this.dni = dni;
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.nationality = nationality;
        this.birthdate = birthdate;
    }

    // Getters
    public String getDNI() {
        return this.dni;
    }

    public int getPhone() {
        return this.phone;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getNationality() {
        return this.nationality;
    }

    public LocalDate getBirthdate() {
        return this.birthdate;
    }

    // Setters
    public void setDNI(String dni) {
        this.dni = dni;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }


    // Print the instance
    public String toString() {
        return "Passenger{" +
                "dni='" + dni + '\'' +
                ", phone=" + phone +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", nationality='" + nationality + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }
}
