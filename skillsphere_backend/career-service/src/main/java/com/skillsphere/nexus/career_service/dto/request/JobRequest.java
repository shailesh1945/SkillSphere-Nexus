package com.skillsphere.nexus.career_service.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {

    private String title;

    private String department;

    private String requiredSkills;

    private Integer minimumExperience;
}