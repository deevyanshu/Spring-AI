package com.fitness.aiservice.Controller;

import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.service.ActivityAiService;
import com.fitness.aiservice.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/recommendation")
public class RecommendationController {
    private final RecommendationService service;

    private final ActivityAiService aiService;

    public RecommendationController(RecommendationService service, ActivityAiService aiService) {
        this.service = service;
        this.aiService = aiService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getUserRecommendation(@PathVariable String userId)
    {
        return ResponseEntity.ok(service.getUserRecommendation(userId));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getActivityRecommendation(@PathVariable(name = "activityId") String activityId)
    {
        return ResponseEntity.ok(service.getActivityRecommendation(activityId));
    }

    @GetMapping("/generate/{activityId}")
    public ResponseEntity<Recommendation> generateRecommendation(@PathVariable(name = "activityId") String activityId)
    {
        return ResponseEntity.ok(aiService.generateRecommendation(activityId));
    }

}
