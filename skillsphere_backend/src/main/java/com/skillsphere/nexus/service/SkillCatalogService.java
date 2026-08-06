package com.skillsphere.nexus.service;

import com.skillsphere.nexus.dto.response.SkillResponse;

import java.util.List;

public interface SkillCatalogService {

    List<SkillResponse> getAllSkills();

    SkillResponse addSkill(SkillResponse dto);
}
