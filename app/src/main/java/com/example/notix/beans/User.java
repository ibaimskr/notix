package com.example.notix.beans;

import java.io.Serializable;

public class User implements Serializable {
    String dni;
    int is_enabled;
    String password;

    public User(String dni, int is_enabled, String password) {
        this.dni = dni;
        this.is_enabled = is_enabled;
        this.password = password;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getIs_enabled() {
        return is_enabled;
    }

    public void setIs_enabled(int is_enabled) {
        this.is_enabled = is_enabled;
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
                "," + '"' + "enabled" + '"' + ":" + '"'+ is_enabled + '"' +
                "}";
    }
}

