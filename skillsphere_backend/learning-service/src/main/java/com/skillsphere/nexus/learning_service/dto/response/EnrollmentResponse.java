package com.skillsphere.nexus.learning_service.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {

    private UUID enrollmentId;

    private UUID empId;

    private UUID courseId;

    private String courseTitle;

    private LocalDateTime enrolledAt;

    private Integer progress;

    private Boolean completed;

    private Float score;

    private LocalDateTime completedAt;
}