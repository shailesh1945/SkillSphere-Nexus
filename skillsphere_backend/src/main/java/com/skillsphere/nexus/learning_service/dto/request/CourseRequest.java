package com.skillsphere.nexus.learning_service.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequest {

    private String title;

    private String description;

    private Integer duration;

    private String type;

    private String instructor;

    private Double rating;
}