package com.example.restAWS.models;

import java.security.SecureRandom;
import java.util.UUID;
    
public class SesionModel {
    private String id;
    private long fecha;
    private long alumnoId;
    private boolean active;
    private String sessionString;

    public SesionModel() {}
    public SesionModel(long alumnoId) {
        this.id = UUID.randomUUID().toString();
        this.fecha = System.currentTimeMillis();
        this.alumnoId = alumnoId;
        this.active = true;
        this.sessionString = generarCadena();
    }

    private String generarCadena() {
        int length = 128;
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = upperCase.toLowerCase();
        String digits = "0123456789";

        String combinedChars = upperCase + lowerCase + digits;
        SecureRandom random = new SecureRandom();

        return random.ints(length, 0, combinedChars.length())
            .mapToObj(combinedChars::charAt)
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(int alumnoId) {
        this.alumnoId = alumnoId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSessionString() {
        return sessionString;
    }

    public void setSessionString(String sessionString) {
        this.sessionString = sessionString;
    }
}
