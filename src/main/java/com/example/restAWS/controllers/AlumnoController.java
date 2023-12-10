package com.example.restAWS.controllers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.example.restAWS.models.SesionModel;
import com.example.restAWS.models.SesionResponse;
import com.example.restAWS.services.AlumnoService;
import com.example.restAWS.services.DynamoService;
import com.example.restAWS.services.AmazonS3Service;
import com.example.restAWS.services.SnsService;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
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

    @PostMapping("/alumnos/{id}/session/login")
    public ResponseEntity<SesionResponse> crearSesionAlumno(@RequestBody Map<String, String> requestBody, @PathVariable Long id){
        try{
            long idLong = id;
            AlumnoModel alumnoEncontrado = alumnoService.obtenerAlumno(idLong);
            
            if(alumnoEncontrado.getId() != -1){
                String contraseniaEnviada = requestBody.get("password");
                if(contraseniaEnviada.equals(alumnoEncontrado.getPassword())){
                    SesionModel sesionModel = new SesionModel(idLong);
                    DynamoService alumnoSesion = new DynamoService();

                    Map<String, AttributeValue> item = new HashMap<>();
                    item.put("id", AttributeValue.builder().s(sesionModel.getId()).build());
                    item.put("fecha", AttributeValue.builder().n(Long.toString(sesionModel.getFecha())).build());
                    item.put("alumnoId", AttributeValue.builder().n(Long.toString(sesionModel.getAlumnoId())).build());
                    item.put("active", AttributeValue.builder().bool(sesionModel.isActive()).build());
                    item.put("sessionString", AttributeValue.builder().s(sesionModel.getSessionString()).build());

                    PutItemRequest putItemRequest = PutItemRequest.builder()
                        .tableName("sesiones-alumnos")
                        .item(item)
                        .build();

                    alumnoSesion.getDynamoClient().putItem(putItemRequest);

                    return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new SesionResponse(sesionModel.getSessionString()));
                }else{
                    return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .build();   
                }
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

    @PostMapping("/alumnos/{id}/session/verify")
    public ResponseEntity verificarSesion(@RequestBody Map<String, String> sessionString, @PathVariable Long id) {
        try {
            long idLong = id;
            String sessionStringParametro = sessionString.get("sessionString");
            DynamoService dynamoCliente = new DynamoService();
            
            String expression = "#alumnoId = :alumnoId";
            Map<String, String> expressionNames = new HashMap<>();
            expressionNames.put("#alumnoId", "alumnoId");

            Map<String, AttributeValue> expressionValues = new HashMap<>();
            expressionValues.put(":alumnoId", AttributeValue.builder().n(String.valueOf(idLong)).build());
            
            ScanRequest scanRequest = ScanRequest.builder()
                .tableName(dynamoCliente.getTableName())
                .filterExpression(expression)
                .expressionAttributeNames(expressionNames)
                .expressionAttributeValues(expressionValues)
                .build();
            
            ScanResponse scanResponse = dynamoCliente.getDynamoClient().scan(scanRequest);

            if (scanResponse.items() != null && !scanResponse.items().isEmpty()) {
                if (isSesionValid(scanResponse.items(), sessionStringParametro)) {
                    return ResponseEntity 
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .build();
                } else {
                    return ResponseEntity 
                        .status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .build();
                }  
            } else {
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

    @PostMapping("/alumnos/{id}/session/logout")    
    public ResponseEntity cerrarSesion(@RequestBody Map<String, String> sessionString, @PathVariable Long id) {
        try {
            long idLong = id;
            DynamoService dynamoCliente = new DynamoService();
            
            String expression = "#alumnoId = :alumnoId";
            Map<String, String> expressionNames = new HashMap<>();
            expressionNames.put("#alumnoId", "alumnoId");

            Map<String, AttributeValue> expressionValues = new HashMap<>();
            expressionValues.put(":alumnoId", AttributeValue.builder().n(String.valueOf(idLong)).build());
            
            ScanRequest scanRequest = ScanRequest.builder()
                .tableName(dynamoCliente.getTableName())
                .filterExpression(expression)
                .expressionAttributeNames(expressionNames)
                .expressionAttributeValues(expressionValues)
                .build();
            
            ScanResponse scanResponse = dynamoCliente.getDynamoClient().scan(scanRequest);

            if (scanResponse.items() != null && !scanResponse.items().isEmpty()) {
                DynamoService alumnoSesion = new DynamoService();
                
                Map<String, AttributeValue> item = scanResponse.items().get(0);
                AttributeValue idValue = item.get("id");
                String idTablaDynamo = idValue.s();

                Boolean nuevoValorActive = false;
                
                UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                    .tableName(alumnoSesion.getTableName())
                    .key(Map.of("id", AttributeValue.builder().s(idTablaDynamo).build()))
                    .updateExpression("SET active = :nuevoValorActive")
                    .expressionAttributeValues(Map.of(":nuevoValorActive", AttributeValue.builder().bool(nuevoValorActive).build()))
                    .build();
                alumnoSesion.getDynamoClient().updateItem(updateItemRequest);
                
                alumnoSesion.getDynamoClient().close();
                
                return ResponseEntity 
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .build();
            } else {
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

    private boolean isSesionValid(List<Map<String, AttributeValue>> scanResponse, String sesionStringEnviada) {
        if (!scanResponse.isEmpty()) {
            Map<String, AttributeValue> item = scanResponse.get(0);
            
            AttributeValue activeValue = item.get("active");
            AttributeValue sessionStringValue = item.get("sessionString");
    
            if (sessionStringValue != null && activeValue != null &&
                sessionStringValue.s() != null && activeValue.bool() != null) {
                return activeValue.bool() && sessionStringValue.s().equals(sesionStringEnviada);
            }
        }
    
        return false;
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