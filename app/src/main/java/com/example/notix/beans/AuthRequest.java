package com.example.notix.beans;

import java.io.Serializable;

public class AuthRequest implements Serializable {

    private String dni;
    private String password;

    int roleId;

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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "{" +
                '"' + "dni" + '"' + ":" + '"' + dni + '"' +
                "," + '"' + "password" + '"' + ":" + '"' + password + '"' +
                "," + '"' + "roleId" + '"' + ":" + '"'+ roleId + '"' +
                '}';
    }

}
