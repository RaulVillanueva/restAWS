package com.example.restAWS.models;

public class AlumnoModel {
    private int id;
    private String nombres;
    private String apellidos;
    private String matricula;
    private Double promedio;
  
    public AlumnoModel(int id, String nombres, String apellidos, String matricula, Double promedio){
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.matricula = matricula;
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
    public double getPromedio() {
        return promedio;
    }
}
