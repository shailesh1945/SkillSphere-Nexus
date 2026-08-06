package com.skillsphere.nexus.service.impl;

import com.skillsphere.nexus.dto.response.CompetencyGapResponse;
import com.skillsphere.nexus.model.CompetencyFramework;
import com.skillsphere.nexus.model.SkillCompetency;
import com.skillsphere.nexus.repository.CompetencyFrameworkRepository;
import com.skillsphere.nexus.repository.SkillCompetencyRepository;
import com.skillsphere.nexus.service.CompetencyMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompetencyMappingServiceImpl
        implements CompetencyMappingService {

    private final SkillCompetencyRepository competencyRepository;
    private final CompetencyFrameworkRepository frameworkRepository;

    @Override
    public List<CompetencyGapResponse> getCompetencyGap(
            UUID employeeId,
            String roleTitle) {

        List<SkillCompetency> employeeSkills =
                competencyRepository.findByEmployeeEmployeeId(employeeId);

        List<CompetencyFramework> frameworks =
                frameworkRepository.findByRoleTitle(roleTitle);

        return frameworks.stream().map(framework -> {

            Integer current = employeeSkills.stream()

                    .filter(skill -> skill.getSkill()
                            .getSkillId()
                            .equals(framework.getSkill().getSkillId()))

                    .findFirst()

                    .map(SkillCompetency::getProficiencyLevel)

                    .orElse(0);

            return CompetencyGapResponse.builder()

                    .skillName(framework.getSkill().getSkillName())

                    .currentProficiency(current)

                    .requiredProficiency(
                            framework.getRequiredProficiency())

                    .gap(
                            framework.getRequiredProficiency()
                                    - current)

                    .build();

        }).toList();
    }
}