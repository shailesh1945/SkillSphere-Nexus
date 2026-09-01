package com.skillsphere.nexus.skill_service.service;

import com.skillsphere.nexus.skill_service.dto.request.SkillRequest;
import com.skillsphere.nexus.skill_service.dto.response.SkillResponse;

import java.util.List;
import java.util.UUID;

public interface SkillCatalogService {

    SkillResponse addSkill(SkillRequest request);

    SkillResponse updateSkill(UUID skillId, SkillRequest request);

    SkillResponse getSkillById(UUID skillId);

    List<SkillResponse> getAllSkills();

    void deleteSkill(UUID skillId);



    List<SkillResponse> getSkillsByCategory(String category);

    List<SkillResponse> searchSkills(String keyword);

    boolean skillExists(String skillName);

    long getSkillCount();
}
