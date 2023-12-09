package com.example.restAWS.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "alumnos")
public class AlumnoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private int id;

    private String nombres;

    private String apellidos;
    
    private String matricula;

    private String password;

    private String fotoPerfilUrl;
    
    private Double promedio;
  
    public AlumnoModel(){}
    public AlumnoModel(int id, String nombres, String apellidos, String matricula, String password, String fotoPerfilUrl,  Double promedio){
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.matricula = matricula;
        this.password = password;
        this.fotoPerfilUrl = fotoPerfilUrl;
        this.promedio = promedio;
    }
   
    public void setId(int id) {
        this.id = id;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }  
    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }
    
    public int getId() {
        return id;
    }
    public String getNombres() {
        return nombres;
    }
    public String getApellidos() {
        return apellidos;
    }
    public String getMatricula() {
        return matricula;
    }
    public String getPassword() {
        return password;
    }
    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }
    public double getPromedio() {
        return promedio;
    }
}