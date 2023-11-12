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

import com.example.restAWS.models.ProfesorModel;

@RestController
@RequestMapping
public class ProfesorController {
    public static List<ProfesorModel> listaProfesores = new ArrayList<>();
 
    @GetMapping("/profesores") 
    public ResponseEntity<List<ProfesorModel>> obtenerProfesores(){
        try{
            return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(listaProfesores);
        } catch (Exception e) {
           return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
        }
    }

    @GetMapping("/profesores/{id}")
    public ResponseEntity<ProfesorModel> obtenerProfesor(@PathVariable Long id) {
        try{
            for (ProfesorModel profesor : listaProfesores) {
                if (profesor.getId() == id) {
                    return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(profesor);
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

    @PostMapping("/profesores")
    public ResponseEntity<ProfesorModel> agregarProfesor(@RequestBody ProfesorModel profesorRequest){
        try {
            HttpStatus status = isValidProfesor(profesorRequest);
            if (status == HttpStatus.OK) {
                listaProfesores.add(new ProfesorModel(profesorRequest.getId(), profesorRequest.getNumeroEmpleado(), profesorRequest.getNombres(), profesorRequest.getApellidos(), profesorRequest.getHorasClase()));
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

    @PutMapping("/profesores/{id}")
    public ResponseEntity<ProfesorModel> actualizarProfesor(@PathVariable Long id, @RequestBody ProfesorModel profesorRequest) {
        try {
            HttpStatus status = isValidProfesor(profesorRequest);
            if (status == HttpStatus.OK) {
                Iterator<ProfesorModel> iterator = listaProfesores.iterator();
                while (iterator.hasNext()) {
                    ProfesorModel profesor = iterator.next();
                    if (profesor.getId() == id) {
                        profesor.setNumeroEmpleado(profesorRequest.getNumeroEmpleado());
                        profesor.setNombres(profesorRequest.getNombres());
                        profesor.setApellidos(profesorRequest.getApellidos());
                        profesor.setHorasClase(profesorRequest.getHorasClase());
                        return ResponseEntity
                            .status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(profesor);
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
    
    @DeleteMapping("/profesores/{id}")
    public ResponseEntity<ProfesorModel> eliminarProfesor(@PathVariable Long id){
        try{
            Iterator<ProfesorModel> iterator = listaProfesores.iterator();
            while (iterator.hasNext()) {
                ProfesorModel profesor = iterator.next();
                if (profesor.getId() == id) {
                    iterator.remove();
                    return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(profesor);
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
    
    private HttpStatus isValidProfesor(ProfesorModel rawProfesor){
        if(!(rawProfesor.getNombres() instanceof String && rawProfesor.getNombres() != null && rawProfesor.getNombres() != "")){
            return HttpStatus.BAD_REQUEST;
        }
        if(!(rawProfesor.getApellidos() instanceof String && rawProfesor.getApellidos() != null && rawProfesor.getApellidos() != "")){
            return HttpStatus.BAD_REQUEST;
        }
        if(!(rawProfesor.getHorasClase() >= 0)){
            return HttpStatus.BAD_REQUEST;
        }
        if(!(rawProfesor.getNumeroEmpleado() >= 0)){
           return HttpStatus.BAD_REQUEST; 
        }
        return HttpStatus.OK;
    }
}