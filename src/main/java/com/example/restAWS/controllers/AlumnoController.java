package com.example.restAWS.controllers;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.restAWS.models.AlumnoModel;
import com.example.restAWS.services.AlumnoService;
import com.example.restAWS.services.AmazonS3Service;
import com.example.restAWS.services.SnsService;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@RestController
@RequestMapping
public class AlumnoController {
    @Autowired
    private AlumnoService alumnoService;

    @GetMapping("/alumnos") 
    public ResponseEntity<List<AlumnoModel>> obtenerAlumnos(){
        try{
            List<AlumnoModel> alumnos = alumnoService.obtenerAlumnos();
            return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(alumnos);
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
            long idLong = id;
            AlumnoModel alumnoEncontrado = alumnoService.obtenerAlumno(idLong);
            
            if(alumnoEncontrado.getId() != -1){
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(alumnoEncontrado);
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

    @PostMapping("/alumnos")
    public ResponseEntity<AlumnoModel> agregarAlumno(@RequestBody AlumnoModel alumnoRequest){
        try {
            HttpStatus status = isValidAlumno(alumnoRequest);
            if (status == HttpStatus.OK) {
                AlumnoModel alumnoCreado = alumnoService.agregarAlumno(alumnoRequest);
                return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(alumnoCreado);
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

    @PostMapping("/alumnos/{id}/fotoPerfil")
    public ResponseEntity<AlumnoModel> subirFotoPerfil(@RequestParam("foto") MultipartFile foto, @PathVariable Long id){
        try{
            long idLong = id;
            AlumnoModel alumnoEncontrado = alumnoService.obtenerAlumno(idLong);
            
            if(alumnoEncontrado.getId() != -1){
                AmazonS3Service amazonS3Service = new AmazonS3Service();
                File convertedFile = convertMultiPartToFile(foto);
                String key = "foto-" + alumnoEncontrado.getMatricula() + "-" + alumnoEncontrado.getApellidos() + "-" + alumnoEncontrado.getNombres();
                
                amazonS3Service.getS3Client().putObject(PutObjectRequest.builder()
                    .bucket(amazonS3Service.getBucketName())
                    .key(key)
                    .build(), convertedFile.toPath());
                
                alumnoEncontrado.setFotoPerfilUrl("https://" + amazonS3Service.getBucketName() + ".s3.amazonaws.com/" + key);
                return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(alumnoService.agregarAlumno(alumnoEncontrado));
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

    @PostMapping("/alumnos/{id}/email") 
    public ResponseEntity<AlumnoModel> enviarNotificacion(@PathVariable Long id){
        try{
            long idLong = id;
            AlumnoModel alumnoEncontrado = alumnoService.obtenerAlumno(idLong);
            
            if(alumnoEncontrado.getId() != -1){
                SnsService snsService = new SnsService();

                String message = "La calificación final del alumno " +
                alumnoEncontrado.getNombres() + " " +
                alumnoEncontrado.getApellidos() + 
                " es de: " + alumnoEncontrado.getPromedio();
                
                snsService.getSnsClient().publish(PublishRequest.builder()
                    .topicArn(snsService.getTopicARN())
                    .message(message)
                    .build());

                return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(alumnoService.agregarAlumno(alumnoEncontrado));
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

    @PutMapping("/alumnos/{id}")
    public ResponseEntity<AlumnoModel> actualizarAlumno(@PathVariable Long id, @RequestBody AlumnoModel alumnoRequest) {
        try {
            HttpStatus status = isValidAlumno(alumnoRequest);
            if (status == HttpStatus.OK) {
                AlumnoModel alumnoEncontrado = alumnoService.obtenerAlumno(id);
                if(alumnoEncontrado != null) {
                    alumnoRequest.setId(Long.valueOf(id).intValue());
                    AlumnoModel alumnoActualizado = alumnoService.agregarAlumno(alumnoRequest);
                    return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(alumnoActualizado);
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
    
    @DeleteMapping("/alumnos/{id}")
    public ResponseEntity<AlumnoModel> eliminarAlumno(@PathVariable Long id){
        try{
            long idLong = id;
            AlumnoModel alumnoEncontrado = alumnoService.obtenerAlumno(idLong);
            
            if(alumnoEncontrado.getId() != -1){
                alumnoService.eliminarAlumno(id);
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

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convertedFile);
        return convertedFile;
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