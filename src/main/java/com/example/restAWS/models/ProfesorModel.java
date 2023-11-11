package com.example.restAWS.models;

public class ProfesorModel {
    private int id;
    private int numeroEmpleado;
    private String nombres;
    private String apellidos;
    private int horasClase;
  
    public ProfesorModel(int id, int numeroEmpleado, String nombres, String apellidos, int horasClase){
        this.id = id;
        this.numeroEmpleado = numeroEmpleado;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.horasClase = horasClase;
    }
   
    public void setId(int id) {
        this.id = id;
    }
    public void setNumeroEmpleado(int numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }       
    public void setHorasClase(int horasClase) {
        this.horasClase = horasClase;
    }
    
    public int getId() {
        return id;
    }  
    public int getNumeroEmpleado() {
        return numeroEmpleado;
    }
    public String getNombres() {
        return nombres;
    }
    public String getApellidos() {
        return apellidos;
    }
    public int getHorasClase() {
        return horasClase;
    }
}
