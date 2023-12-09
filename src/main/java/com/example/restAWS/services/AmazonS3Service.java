package com.example.restAWS.services;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import org.springframework.stereotype.Service;

@Service
public class AmazonS3Service {
    private S3Client s3Client;
    private final String BUCKET_NAME = "restapivillanuevaraulbucket";
    private final String ACCESS_KEY = "ASIA6CRJ335EF3CPMAUV";
    private final String SECRET_KEY = "Hm8PkGFXXJRo2ngDx9srNWwnm5WuExNKtB1UngC+";
    private final String SESSION_TOKEN = "FwoGZXIvYXdzEIH//////////wEaDCGBIsDN4WSyUXznPiLUAXjwBnUIq8ntTlDgFjHfEip9mnsppov8pFIlci8BccmBxP1kqf5lLClm7IJ777IWQ4fzTjiO6XogN3fqXNonljt04vNs8Gv4KBb5AkE+ZlpsallKfTAWZ4ZkqxDOEQNoddHLpcXkShnuA6YzZN+bRl2SlPBtEMMWSlAxuTTFHrXvWcXaN/qFN2FLNXeYT3TLFhtutuOqQz7HJv7sNqnhFCUbtkM7ub1dDCK+QUoMN4RK3OyWy4i70bxK9cJUtPxMSxlzumeGfu0jSK+Sx/SMwGRBCY3tKOiQ0qsGMi0h+t64EVeBBDPTE2KVuwrA9HkyPg21zURc4wykfAq9z3RYwV6rQxeAGKbi17w=";

    public AmazonS3Service() {
        AwsSessionCredentials awsCredentials = AwsSessionCredentials.create(ACCESS_KEY, SECRET_KEY, SESSION_TOKEN);

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