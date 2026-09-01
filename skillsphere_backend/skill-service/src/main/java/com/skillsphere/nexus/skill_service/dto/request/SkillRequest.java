package com.skillsphere.nexus.skill_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SkillRequest {

    @NotBlank
    private String skillName;

    private String description;

    private String category;
}