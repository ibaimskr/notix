package com.example.notix.beans;

import java.io.Serializable;

public class AuthResponse implements Serializable {

    private String dni;
    private String accessToken;
    private int error;

    public AuthResponse() {
    }

    public AuthResponse(String dni, String accessToken) {
        super();
        this.dni = dni;
        this.accessToken = accessToken;
    }

    public String getDni() { return dni; }

    public void setDni(String user) { this.dni = dni; }

    public String getAccessToken() { return accessToken; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

}
