package com.skillsphere.nexus.skill_service.service;


import com.skillsphere.nexus.skill_service.dto.request.EmployeeSkillRequest;

import java.util.Map;
import java.util.UUID;

public interface SkillProfileService {

    Map<String, Object> getSkillProfile(UUID employeeId);

    Map<String, Object> addSkillToProfile(
            UUID employeeId,
            EmployeeSkillRequest request
    );
}