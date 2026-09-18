package com.skillsphere.nexus.learning_service.controller;

import com.skillsphere.nexus.learning_service.model.LearningPath;
import com.skillsphere.nexus.learning_service.model.LearningPathCourse;
import com.skillsphere.nexus.learning_service.service.LearningPathService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/learning/paths")
@RequiredArgsConstructor
public class LearningPathController {

    private final LearningPathService learningPathService;

    @PostMapping
    @PreAuthorize("hasAnyRole('TRAINING_MANAGER', 'ADMIN')")
    public LearningPath createPath(
            @RequestBody LearningPath path) {

        return learningPathService
                .createPath(path);
    }

    @PostMapping("/{pathId}/courses/{courseId}")
    @PreAuthorize("hasAnyRole('TRAINING_MANAGER', 'ADMIN')")
    public  LearningPath addCourse(
            @PathVariable UUID pathId,
            @PathVariable UUID courseId,
            @RequestParam Integer sequence) {

        return learningPathService
                .addCourseToPath(pathId, courseId, sequence);
    }

    @GetMapping("/{pathId}/courses")
    @PreAuthorize("isAuthenticated()")
    public List<LearningPathCourse> getCourses(
            @PathVariable UUID pathId){

        return learningPathService
                .getPathCourses(pathId);
    }

}
