package com.skillsphere.nexus.learning_service.controller;

import com.skillsphere.nexus.learning_service.dto.request.CourseRequest;
import com.skillsphere.nexus.learning_service.dto.response.CourseResponse;
import com.skillsphere.nexus.learning_service.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/learning/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;



    @PostMapping
    @PreAuthorize("hasAnyRole('TRAINING_MANAGER', 'ADMIN')")
    public ResponseEntity<CourseResponse> addCourse(
            @RequestBody CourseRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(courseService.addCourse(request));
    }


    @PutMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('TRAINING_MANAGER', 'ADMIN')")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable UUID courseId,
            @RequestBody CourseRequest request) {

        return ResponseEntity.ok(
                courseService.updateCourse(
                        courseId,
                        request));
    }


    @GetMapping("/{courseId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CourseResponse> getCourseById(
            @PathVariable UUID courseId) {

        return ResponseEntity.ok(
                courseService.getCourseById(courseId));
    }


    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {

        return ResponseEntity.ok(
                courseService.getAllCourses());
    }


    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasAnyRole('TRAINING_MANAGER', 'ADMIN')")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable UUID courseId) {

        courseService.deleteCourse(courseId);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/active")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CourseResponse>> getActiveCourses() {

        return ResponseEntity.ok(
                courseService.getActiveCourses());
    }


    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CourseResponse>> searchCourses(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                courseService.searchCourses(keyword));
    }


    @GetMapping("/instructor")
    @PreAuthorize("hasAnyRole('TRAINING_MANAGER', 'HR', 'ADMIN')")
    public ResponseEntity<List<CourseResponse>>
    getCoursesByInstructor(
            @RequestParam String instructor) {

        return ResponseEntity.ok(
                courseService.getCoursesByInstructor(
                        instructor));
    }


    @GetMapping("/exists")
    @PreAuthorize("hasAnyRole('TRAINING_MANAGER', 'ADMIN')")
    public ResponseEntity<Boolean> courseExists(
            @RequestParam String courseName) {

        return ResponseEntity.ok(
                courseService.courseExists(courseName));
    }


    @GetMapping("/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getCourseCount() {

        return ResponseEntity.ok(
                courseService.getCourseCount());
    }
}