package com.fitness.activityservice.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ValidationService {

    private final WebClient userServieWebClient;

    public ValidationService(WebClient userServieWebClient) {
        this.userServieWebClient = userServieWebClient;
    }


    public boolean validateUser(String userId)
    {
        return userServieWebClient.get().uri("api/v1/users/{userId}/validate",userId).retrieve()
                .bodyToMono(Boolean.class).block(); //this is blocking request
//        return userServieWebClient.get().uri("api/v1/users/{userId}/validate",userId).retrieve()
//                .bodyToMono(Boolean.class); //this is non-blocking request
    }
}
