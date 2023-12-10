package com.example.restAWS.services;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

public class SnsService {
    private SnsClient snsClient;
    private final String TOPIC_ARN = "arn:aws:sns:us-east-1:967529193288:CalificacionesAlumnos";
    private final String ACCESS_KEY = "ASIA6CRJ335EH7ZKRNNG";
    private final String SECRET_KEY = "IuV5nzd/Aolaiav1FQUCreIS3/588hK3cw5SHPRG";
    private final String SESSION_TOKEN = "FwoGZXIvYXdzEI7//////////wEaDCeUmgveGmBuMGIaxyLUATTE52uOtjnxLNTcJBED3m3ZsAAfuTfVhAIowTzKBow/pN/itX4hHsUr+R3pD+RlWnBqAjcKp0jXr6Yu6E9wfE1g1bqYbP6ubKzQRpScwQLVeqTm7SKVbE9F2ksIWmnrqx/HGPaiPRYiueX4xA/1HrYIgc/WHMYKjoIO8Rdv1pGKAoUXT8DzdeO/B0EBf1/gQDWx/rwesc8Uk9TXA6W4g9JQ+pABHMLNYQJ1MYUDMmoceI6X1YOp4dI/hZ+lN4gIWr21rihtXgKjC/OJleRAYzpi3NcHKO2B1asGMi0fluTfASomFdCG1DnJlcxznxIE7Cg9yx/4lwyAJC+5M6DKXs39lfcTQbwQfSI=";
    
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