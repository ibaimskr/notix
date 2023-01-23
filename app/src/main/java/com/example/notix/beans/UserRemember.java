package com.example.notix.beans;

import java.io.Serializable;

public class UserRemember implements Serializable {

    private String dni;
    private String pass;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
