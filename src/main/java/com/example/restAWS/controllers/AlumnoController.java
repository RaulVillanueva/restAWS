package com.example.restAWS.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.restAWS.models.AlumnoModel;

@RestController
@RequestMapping
public class AlumnoController {
    public static List<AlumnoModel> listaAlumnos = new ArrayList<>();
 
    @GetMapping("/alumnos") 
    public ResponseEntity<List<AlumnoModel>> obtenerAlumnos(){
        try{
            return new ResponseEntity<>(listaAlumnos, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
        }
    }

    @GetMapping("/alumnos/{id}")
    public ResponseEntity<AlumnoModel> obtenerAlumno(@PathVariable Long id) {
        try{
            for (AlumnoModel alumno : listaAlumnos) {
                if (alumno.getId() == id) {
                    return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(alumno);
                }
            }
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .build(); 
        }catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
        }
    }

    @PostMapping("/alumnos")
    public ResponseEntity<AlumnoModel> agregarAlumno(@RequestBody AlumnoModel alumnoRequest){
        try {
            HttpStatus status = isValidAlumno(alumnoRequest);
            if (status == HttpStatus.OK) {
                listaAlumnos.add(new AlumnoModel(alumnoRequest.getId(), alumnoRequest.getNombres(), alumnoRequest.getApellidos(), alumnoRequest.getMatricula(), alumnoRequest.getPromedio()));
                return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
            } else {
                return ResponseEntity
                    .status(status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
            }
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
        }
    }

    @PutMapping("/alumnos/{id}")
    public ResponseEntity<AlumnoModel> actualizarAlumno(@PathVariable Long id, @RequestBody AlumnoModel alumnoRequest) {
        try {
            HttpStatus status = isValidAlumno(alumnoRequest);
            if (status == HttpStatus.OK) {
                Iterator<AlumnoModel> iterator = listaAlumnos.iterator();
                while (iterator.hasNext()) {
                    AlumnoModel alumno = iterator.next();
                    if (alumno.getId() == id) {
                        alumno.setNombres(alumnoRequest.getNombres());
                        alumno.setApellidos(alumnoRequest.getApellidos());
                        alumno.setMatricula(alumnoRequest.getMatricula());
                        alumno.setPromedio(alumnoRequest.getPromedio());
                        return ResponseEntity
                            .status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(alumno);
                    }
                }
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .build(); 
            } else {
                return ResponseEntity
                    .status(status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
            }
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
        }
    }
    
    @DeleteMapping("/alumnos/{id}")
    public ResponseEntity<AlumnoModel> eliminarAlumno(@PathVariable Long id){
        try{
            Iterator<AlumnoModel> iterator = listaAlumnos.iterator();
            while (iterator.hasNext()) {
                AlumnoModel alumno = iterator.next();
                if (alumno.getId() == id) {
                    iterator.remove();
                    return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(alumno);
                }
            }
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .build(); 
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
        }   
    }
    
    private HttpStatus isValidAlumno(AlumnoModel rawAlumnno){
        if(!(rawAlumnno.getNombres() instanceof String && rawAlumnno.getNombres() != null && rawAlumnno.getNombres() != "")){
            return HttpStatus.BAD_REQUEST;
        }
        if(!(rawAlumnno.getApellidos() instanceof String && rawAlumnno.getApellidos() != null && rawAlumnno.getApellidos() != "")){
            return HttpStatus.BAD_REQUEST;
        }
        if(!(rawAlumnno.getMatricula() instanceof String && rawAlumnno.getMatricula() != null && rawAlumnno.getMatricula() != "")){
            return HttpStatus.BAD_REQUEST;
        }
        if(!(rawAlumnno.getPromedio() >= 0)){
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }
}