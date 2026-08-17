package com.skillsphere.nexus.skill_service.service;

import com.skillsphere.nexus.skill_service.dto.response.SkillProfileResponse;

import java.util.UUID;

public interface SkillProfileService {

    SkillProfileResponse getSkillProfile(UUID employeeId);

}