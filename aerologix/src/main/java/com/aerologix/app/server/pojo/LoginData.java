package com.aerologix.app.server.pojo;

public class LoginData {

    private String email;
    private String password;

    public LoginData() {
        // required by serialization
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String toString() {
        return "Login [email=" + email + ", password=" + password + "]";
    }

}
