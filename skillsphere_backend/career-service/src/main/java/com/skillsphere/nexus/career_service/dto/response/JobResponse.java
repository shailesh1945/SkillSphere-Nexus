package com.skillsphere.nexus.career_service.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {

    private UUID jobId;

    private String title;

    private String department;

    private String requiredSkills;

    private Integer minimumExperience;

    private Boolean active;
}