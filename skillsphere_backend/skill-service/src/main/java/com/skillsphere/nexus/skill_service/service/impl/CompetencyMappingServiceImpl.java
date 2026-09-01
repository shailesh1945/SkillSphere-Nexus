package com.skillsphere.nexus.skill_service.service.impl;

import com.skillsphere.nexus.skill_service.dto.response.CompetencyGapResponse;
import com.skillsphere.nexus.skill_service.model.CompetencyFramework;
import com.skillsphere.nexus.skill_service.model.EmployeeSkill;
import com.skillsphere.nexus.skill_service.repository.CompetencyFrameworkRepository;
import com.skillsphere.nexus.skill_service.repository.EmployeeSkillRepository;
import com.skillsphere.nexus.skill_service.service.CompetencyMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompetencyMappingServiceImpl
        implements CompetencyMappingService {

    private final EmployeeSkillRepository employeeSkillRepository;
    private final CompetencyFrameworkRepository frameworkRepository;

    @Override
    public List<CompetencyGapResponse> getCompetencyGap(
            UUID employeeId,
            String roleTitle) {

        List<EmployeeSkill> currentSkills =
                employeeSkillRepository
                        .findByEmployeeEmployeeId(employeeId);

        List<CompetencyFramework> frameworks =
                frameworkRepository
                        .findByRoleTitle(roleTitle);

        return frameworks.stream().map(framework -> {

            Integer current = currentSkills.stream()

                    .filter(skill -> skill.getSkill()
                            .getSkillId()
                            .equals(framework.getSkill().getSkillId()))

                    .findFirst()

                    .map(EmployeeSkill::getProficiency)

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