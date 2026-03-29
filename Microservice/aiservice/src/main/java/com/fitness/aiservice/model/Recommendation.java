package com.fitness.aiservice.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String activityId;

    private String userId;

    @Column(name = "recommendation",columnDefinition = "LONGTEXT")
    private String recommendation;

    private String activityType;

    @Column(name = "improvements",columnDefinition = "LONGTEXT")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> improvements;

    @Column(name = "suggestions",columnDefinition = "LONGTEXT")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> suggestions;

    @Column(name = "safety",columnDefinition = "LONGTEXT")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> safety;

    private LocalDateTime createdAt;


    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityType() {
        return activityType;
    }

    public Recommendation(String id, String activityId, String userId, String recommendation, String activityType, List<String> improvements, List<String> suggestions, List<String> safety, LocalDateTime createdAt) {
        this.id = id;
        this.activityId = activityId;
        this.userId = userId;
        this.recommendation = recommendation;
        this.activityType = activityType;
        this.improvements = improvements;
        this.suggestions = suggestions;
        this.safety = safety;
        this.createdAt = createdAt;
    }

    public Recommendation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public List<String> getImprovements() {
        return improvements;
    }

    public void setImprovements(List<String> improvements) {
        this.improvements = improvements;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public List<String> getSafety() {
        return safety;
    }

    public void setSafety(List<String> safety) {
        this.safety = safety;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
