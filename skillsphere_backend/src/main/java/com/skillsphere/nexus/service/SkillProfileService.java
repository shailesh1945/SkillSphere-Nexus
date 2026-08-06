package com.skillsphere.nexus.service;

import com.skillsphere.nexus.dto.response.SkillProfileResponse;

import java.util.UUID;

public interface SkillProfileService {

    SkillProfileResponse getSkillProfile(UUID employeeId);

}