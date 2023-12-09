package com.example.restAWS.services;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

public class SnsService {
    private SnsClient snsClient;
    private final String TOPIC_ARN = "arn:aws:sns:us-east-1:967529193288:CalificacionesAlumnos";
    private final String ACCESS_KEY = "ASIA6CRJ335EF3CPMAUV";
    private final String SECRET_KEY = "Hm8PkGFXXJRo2ngDx9srNWwnm5WuExNKtB1UngC+";
    private final String SESSION_TOKEN = "FwoGZXIvYXdzEIH//////////wEaDCGBIsDN4WSyUXznPiLUAXjwBnUIq8ntTlDgFjHfEip9mnsppov8pFIlci8BccmBxP1kqf5lLClm7IJ777IWQ4fzTjiO6XogN3fqXNonljt04vNs8Gv4KBb5AkE+ZlpsallKfTAWZ4ZkqxDOEQNoddHLpcXkShnuA6YzZN+bRl2SlPBtEMMWSlAxuTTFHrXvWcXaN/qFN2FLNXeYT3TLFhtutuOqQz7HJv7sNqnhFCUbtkM7ub1dDCK+QUoMN4RK3OyWy4i70bxK9cJUtPxMSxlzumeGfu0jSK+Sx/SMwGRBCY3tKOiQ0qsGMi0h+t64EVeBBDPTE2KVuwrA9HkyPg21zURc4wykfAq9z3RYwV6rQxeAGKbi17w=";
    
    public SnsService() {
        AwsSessionCredentials awsCredentials = AwsSessionCredentials.create(ACCESS_KEY, SECRET_KEY, SESSION_TOKEN);

        snsClient = SnsClient.builder()
            .region(Region.US_EAST_1)
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
            .build();
    }

    public SnsClient getSnsClient(){
        return snsClient;
    }

    public String getTopicARN(){
        return TOPIC_ARN;
    }
}