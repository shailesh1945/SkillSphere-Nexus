package com.skillsphere.nexus.learning_service.service;

import com.skillsphere.nexus.learning_service.model.CourseContent;

import java.util.List;
import java.util.UUID;

public interface CourseContentService {

    CourseContent addContent(
            UUID courseId,
            CourseContent content);

    List<CourseContent> getCourseContent(
            UUID courseId);
}