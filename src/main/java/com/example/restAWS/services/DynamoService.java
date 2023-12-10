package com.example.restAWS.services;

import com.example.restAWS.Credenciales;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoService {
    private DynamoDbClient dynamoDbClient;
    private final String TABLA_NAME = "sesiones-alumnos";
     
    public DynamoService() {
        AwsSessionCredentials awsCredentials = AwsSessionCredentials.create(Credenciales.ACCESS_KEY, Credenciales.SECRET_KEY, Credenciales.SESSION_TOKEN);

        dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_1)
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build();
    }

    public DynamoDbClient getDynamoClient(){
        return dynamoDbClient;
    }

    public String getTableName(){
        return TABLA_NAME;
    }
}