package com.example.restAWS.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restAWS.models.ProfesorModel;

@Repository
public interface ProfesorRepository extends JpaRepository<ProfesorModel, Long>{
    
}

