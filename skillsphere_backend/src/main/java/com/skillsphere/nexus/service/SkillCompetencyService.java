package com.skillsphere.nexus.service;

import com.skillsphere.nexus.dto.request.SkillCompetencyRequest;
import com.skillsphere.nexus.dto.response.SkillCompetencyResponse;

import java.util.List;
import java.util.UUID;

public interface SkillCompetencyService {


    SkillCompetencyResponse addCompetency(SkillCompetencyRequest request);

    SkillCompetencyResponse updateCompetency(UUID competencyId,
                                             SkillCompetencyRequest request);

    SkillCompetencyResponse getCompetencyById(UUID competencyId);

    List<SkillCompetencyResponse> getAllCompetencies();

    void deleteCompetency(UUID competencyId);



    List<SkillCompetencyResponse> getCompetenciesByEmployee(UUID employeeId);

    List<SkillCompetencyResponse> getCompetenciesBySkill(UUID skillId);

    List<SkillCompetencyResponse> getCompetenciesByProficiencyLevel(
            Integer proficiencyLevel);

    double calculateAverageExperience(UUID employeeId);

    boolean competencyExists(UUID employeeId, UUID skillId);

}