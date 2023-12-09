package com.example.restAWS.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.restAWS.models.AlumnoModel;
import com.example.restAWS.repositories.AlumnoRepository;

@Service
public class AlumnoService {
    @Autowired
    AlumnoRepository alumnoRepository;
    
    public List<AlumnoModel> obtenerAlumnos(){
        List<AlumnoModel> listaAlumnos = new ArrayList<>();
        alumnoRepository.findAll().forEach(listaAlumnos::add);
        return listaAlumnos;
    }

    public AlumnoModel obtenerAlumno(Long id) {
        Optional<AlumnoModel> alumnoObj = alumnoRepository.findById(id);
        if(alumnoObj.isPresent()){
            return alumnoObj.get();
        }else{
            AlumnoModel alumnoNoEncontrado = new AlumnoModel();
            alumnoNoEncontrado.setId(-1);
            return alumnoNoEncontrado;
        }    
    }

    public AlumnoModel agregarAlumno(AlumnoModel alumnoModel){
        return alumnoRepository.save(alumnoModel);
    }

    public void eliminarAlumno(Long id){
        alumnoRepository.deleteById(id);
    }
}