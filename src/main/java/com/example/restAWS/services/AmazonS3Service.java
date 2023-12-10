package com.example.restAWS.services;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import org.springframework.stereotype.Service;

import com.example.restAWS.Credenciales;

@Service
public class AmazonS3Service {
    private S3Client s3Client;
    private final String BUCKET_NAME = "restapivillanuevaraulbucket";
    
    public AmazonS3Service() {
        AwsSessionCredentials awsCredentials = AwsSessionCredentials.create(Credenciales.ACCESS_KEY, Credenciales.SECRET_KEY, Credenciales.SESSION_TOKEN);
        
        s3Client = S3Client.builder()
            .region(Region.US_EAST_1)
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build();
    }

    public S3Client getS3Client(){
        return s3Client;
    }

    public String getBucketName(){
        return BUCKET_NAME;
    }
}