package com.skillsphere.nexus.learning_service.dto.request;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequest {

    private UUID empId;

    private UUID courseId;
}
