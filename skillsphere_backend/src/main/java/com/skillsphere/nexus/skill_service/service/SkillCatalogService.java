package com.skillsphere.nexus.skill_service.service;

import com.skillsphere.nexus.skill_service.dto.response.SkillResponse;

import java.util.List;

public interface SkillCatalogService {

    List<SkillResponse> getAllSkills();

    SkillResponse addSkill(SkillResponse dto);
}
