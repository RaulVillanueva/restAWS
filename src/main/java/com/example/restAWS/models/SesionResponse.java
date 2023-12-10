package com.example.restAWS.models;

public class SesionResponse {
    private String sessionString;

    public SesionResponse(String sessionString) {
        this.sessionString = sessionString;
    }

    public String getSessionString() {
        return sessionString;
    }

    public void setSessionString(String sessionString) {
        this.sessionString = sessionString;
    }
}

