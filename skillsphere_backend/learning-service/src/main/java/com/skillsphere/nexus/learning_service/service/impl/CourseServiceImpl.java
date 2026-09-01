package com.skillsphere.nexus.learning_service.service.impl;

import com.skillsphere.nexus.learning_service.dto.request.CourseRequest;
import com.skillsphere.nexus.learning_service.dto.response.CourseResponse;
import com.skillsphere.nexus.learning_service.model.Course;
import com.skillsphere.nexus.learning_service.repository.CourseRepository;
import com.skillsphere.nexus.learning_service.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;


    @Override
    public CourseResponse addCourse(CourseRequest request) {

        if (courseRepository.existsByTitleIgnoreCase(
                request.getTitle())) {

            throw new RuntimeException(
                    "Course already exists: "
                            + request.getTitle());
        }

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .duration(request.getDuration())
                .type(Course.CourseType.valueOf(
                        request.getType().toUpperCase()))
                .instructor(request.getInstructor())
                .rating(request.getRating())
                .active(true)
                .build();

        Course savedCourse =
                courseRepository.save(course);

        return mapToResponse(savedCourse);
    }


    @Override
    public CourseResponse updateCourse(
            UUID courseId,
            CourseRequest request) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Course not found with id: "
                                        + courseId));

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setInstructor(request.getInstructor());
        course.setRating(request.getRating());

        Course updatedCourse =
                courseRepository.save(course);

        return mapToResponse(updatedCourse);
    }


    @Override
    public CourseResponse getCourseById(UUID courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Course not found with id: "
                                        + courseId));

        return mapToResponse(course);
    }


    @Override
    public List<CourseResponse> getAllCourses() {

        return courseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public void deleteCourse(UUID courseId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Course not found with id: "
                                        + courseId));

        courseRepository.delete(course);
    }


    @Override
    public List<CourseResponse> getActiveCourses() {

        return courseRepository
                .findByActive(true)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public List<CourseResponse> searchCourses(
            String keyword) {

        return courseRepository
                .findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public List<CourseResponse> getCoursesByInstructor(
            String instructor) {

        return courseRepository
                .findByInstructorContainingIgnoreCase(instructor)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public boolean courseExists(String title) {

        return courseRepository
                .existsByTitleIgnoreCase(title);
    }


    @Override
    public long getCourseCount() {

        return courseRepository.count();
    }


    // -------------------------
    // Helper Method
    // -------------------------

    private CourseResponse mapToResponse(Course course) {

        return CourseResponse.builder()
                .courseId(course.getCourseId())
                .title(course.getTitle())
                .description(course.getDescription())
                .duration(course.getDuration())
                .type(course.getType().name())
                .instructor(course.getInstructor())
                .rating(course.getRating())
                .active(course.getActive())
                .build();
    }
}