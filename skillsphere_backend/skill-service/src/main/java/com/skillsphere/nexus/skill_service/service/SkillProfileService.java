package com.skillsphere.nexus.skill_service.service;


import java.util.Map;
import java.util.UUID;

public interface SkillProfileService {

    Map<String, Object> getSkillProfile(UUID employeeId);

}