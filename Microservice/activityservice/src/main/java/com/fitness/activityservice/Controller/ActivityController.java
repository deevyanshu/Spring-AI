package com.fitness.activityservice.Controller;

import com.fitness.activityservice.Model.Activity;
import com.fitness.activityservice.Service.ActivityService;
import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/activities")
public class ActivityController {

    private ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/")
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest activityRequest)
    {
        return ResponseEntity.ok(activityService.trackActivity(activityRequest));
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<Activity> getActivity(@PathVariable(name = "activityId") String activityId)
    {
        return ResponseEntity.ok(activityService.getActivityById(activityId));
    }


}
