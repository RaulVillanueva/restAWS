package com.example.restAWS.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.restAWS.models.ProfesorModel;
import com.example.restAWS.repositories.ProfesorRepository;

@Service
public class ProfesorService {
    @Autowired
    ProfesorRepository profesorRepository;

    public List<ProfesorModel> obtenerProfesores(){
        List<ProfesorModel> listaProfesores = new ArrayList<>();
        profesorRepository.findAll().forEach(listaProfesores::add);
        return listaProfesores;
    }

    public ProfesorModel obtenerProfesor(Long id) {
        Optional<ProfesorModel> profesorObj = profesorRepository.findById(id);
        if(profesorObj.isPresent()){
            return profesorObj.get();
        }else{
            ProfesorModel profesorNoEncontrado = new ProfesorModel();
            profesorNoEncontrado.setId(-1);
            return profesorNoEncontrado;
        }    
    }

    public ProfesorModel agregarProfesor(ProfesorModel profesorModel){
        return profesorRepository.save(profesorModel);
    }

    public void eliminarProfesor(Long id){
        profesorRepository.deleteById(id);
    }
}