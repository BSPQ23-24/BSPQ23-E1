package com.aerologix.app.server.pojo;

public class PassengerData {
    protected String dni;
	protected int phone;
	protected String name;
	protected String email;
	protected String nationality;
	protected long birthdate;

    public PassengerData() {
        // required by serialization
    }

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

    public long getBirthdate() {
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

    public void setBirthdate(long birthdate) {
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
