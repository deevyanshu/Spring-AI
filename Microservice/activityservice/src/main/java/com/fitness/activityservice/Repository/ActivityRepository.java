package com.fitness.activityservice.Repository;

import com.fitness.activityservice.Model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,String> {
}
