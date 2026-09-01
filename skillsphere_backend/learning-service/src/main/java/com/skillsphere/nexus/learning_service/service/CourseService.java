package com.skillsphere.nexus.learning_service.service;

import com.skillsphere.nexus.learning_service.dto.request.CourseRequest;
import com.skillsphere.nexus.learning_service.dto.response.CourseResponse;

import java.util.List;
import java.util.UUID;

public interface CourseService {

    CourseResponse addCourse(CourseRequest request);

    CourseResponse updateCourse(
            UUID courseId,
            CourseRequest request);

    CourseResponse getCourseById(UUID courseId);

    List<CourseResponse> getAllCourses();

    void deleteCourse(UUID courseId);

    List<CourseResponse> getActiveCourses();

    List<CourseResponse> searchCourses(String keyword);

    List<CourseResponse> getCoursesByInstructor(
            String instructor);

    boolean courseExists(String title);

    long getCourseCount();
}