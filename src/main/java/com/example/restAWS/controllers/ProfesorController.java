package com.example.restAWS.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.restAWS.services.ProfesorService;

@RestController
@RequestMapping
public class ProfesorController {
    @Autowired
    private ProfesorService profesorService;

    @GetMapping("/profesores") 
    public ResponseEntity<List<ProfesorModel>> obtenerProfesores(){
        try{
            List<ProfesorModel> profesores = profesorService.obtenerProfesores();
            return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(profesores);
        }catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
        }
    }

    @GetMapping("/profesores/{id}")
    public ResponseEntity<ProfesorModel> obtenerProfesor(@PathVariable Long id) {
        try{
            long idLong = id;
            ProfesorModel profesorEncontrado = profesorService.obtenerProfesor(idLong);
            
            if(profesorEncontrado.getId() != -1){
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(profesorEncontrado);
            }else{
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .build(); 
            }
        }catch (Exception e) {
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
                ProfesorModel profesorCreado = profesorService.agregarProfesor(profesorRequest);
                return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(profesorCreado);
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
                ProfesorModel profesorEncontrado = profesorService.obtenerProfesor(id);
                if(profesorEncontrado != null) {
                    profesorRequest.setId(Long.valueOf(id).intValue());
                    ProfesorModel profesorActualizado = profesorService.agregarProfesor(profesorRequest);
                    return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(profesorActualizado);
                }else{
                    return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .build(); 
                }
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
            long idLong = id;
            ProfesorModel profesorEncontrado = profesorService.obtenerProfesor(idLong);
            
            if(profesorEncontrado.getId() != -1){
                profesorService.eliminarProfesor(id);
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
            }else{
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
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