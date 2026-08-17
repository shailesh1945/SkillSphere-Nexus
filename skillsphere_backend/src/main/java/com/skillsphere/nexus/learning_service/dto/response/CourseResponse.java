package com.skillsphere.nexus.learning_service.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {

    private UUID courseId;

    private String title;

    private String description;

    private Integer duration;

    private String type;

    private String instructor;

    private Double rating;

    private Boolean active;
}