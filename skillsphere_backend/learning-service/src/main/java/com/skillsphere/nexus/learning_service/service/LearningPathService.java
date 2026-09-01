package com.skillsphere.nexus.learning_service.service;



import com.skillsphere.nexus.learning_service.model.LearningPath;
import com.skillsphere.nexus.learning_service.model.LearningPathCourse;

import java.util.List;
import java.util.UUID;

public interface LearningPathService {

    LearningPath createPath(
           LearningPath path);

    LearningPath addCourseToPath(
            UUID pathId, UUID courseId, Integer sequence);

    List<LearningPathCourse> getPathCourses(
            UUID pathId);
}