package com.example.notix.beans;

import java.io.Serializable;

public class AuthResponse implements Serializable {

    private String dni;
    private String accessToken;
    private int roleId;
    private int error;

    public AuthResponse() { }

    public AuthResponse(String dni, String accessToken, int roleId) {
        super();
        this.dni = dni;
        this.accessToken = accessToken;
        this.roleId = roleId;
    }

    public String getDni() { return dni; }

    public void setDni(String dni) { this.dni = dni; }

    public String getAccessToken() { return accessToken; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getRoleId() { return roleId; }

    public void setRoleId(int roleId) { this.roleId = roleId; }
}
