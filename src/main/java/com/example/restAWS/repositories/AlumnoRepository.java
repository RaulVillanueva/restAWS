package com.example.restAWS.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restAWS.models.AlumnoModel;

@Repository
public interface AlumnoRepository extends JpaRepository<AlumnoModel, Long>{
    
}
