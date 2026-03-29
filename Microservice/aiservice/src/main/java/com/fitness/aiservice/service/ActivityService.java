package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ActivityService {

    private final WebClient webClient;

    public ActivityService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Activity getActivity(String activityId)
    {
        return webClient.get().uri("api/v1/activities/{activityId}",activityId).retrieve()
                .bodyToMono(Activity.class).block();
    }
}
