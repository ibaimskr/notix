package com.example.notix.beans;

import java.io.Serializable;

public class AuthRequest implements Serializable {

    private String dni;
    private String password;

    public AuthRequest() {
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "{" +
                '"' + "dni" + '"' + ":" + '"' + dni + '"' +
                "," + '"' + "password" + '"' + ":" + '"' + password + '"' +
                '}';
    }

}
