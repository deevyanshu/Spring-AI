package com.fitness.activityservice.Service;

import com.fitness.activityservice.Model.Activity;
import com.fitness.activityservice.Repository.ActivityRepository;
import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
    private final ActivityRepository repository;
    private final ValidationService validationService;

    public ActivityService(ActivityRepository repository, ValidationService validationService) {
        this.repository = repository;
        this.validationService = validationService;
    }

    public ActivityResponse trackActivity(ActivityRequest activityRequest)
    {
        boolean isValidUser= validationService.validateUser(activityRequest.getUserId());

        if(!isValidUser)
        {
            throw new RuntimeException("Invalid user: " + activityRequest.getUserId());
        }

        Activity activity=new Activity();


        activity.setUserId(activityRequest.getUserId());
        activity.setType(activityRequest.getType());
        activity.setDuration(activityRequest.getDuration());
        activity.setCaloriesBurned(activityRequest.getCaloriesBurned());
        activity.setStartTime(activityRequest.getStartTime());
        activity.setAdditionalMetrics(activityRequest.getAdditionalMetrics());

        Activity savedActivity=repository.save(activity);


        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity savedActivity) {
        ActivityResponse response=new ActivityResponse();
        response.setId(savedActivity.getId());
        response.setUserId(savedActivity.getUserId());
        response.setType(savedActivity.getType());
        response.setDuration(savedActivity.getDuration());
        response.setCreatedAt(savedActivity.getCreatedAt());
        response.setCaloriesBurned(savedActivity.getCaloriesBurned());
        response.setStartTime(savedActivity.getStartTime());
        response.setAdditionalMetrics(savedActivity.getAdditionalMetrics());
        response.setUpdatedAt(savedActivity.getUpdatedAt());
        return response;
    }

    public Activity getActivityById(String activityId)
    {
        return repository.findById(activityId).get();
    }
}
