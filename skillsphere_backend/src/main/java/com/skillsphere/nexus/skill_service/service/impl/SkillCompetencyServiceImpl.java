package com.skillsphere.nexus.skill_service.service.impl;

import com.skillsphere.nexus.skill_service.dto.request.SkillCompetencyRequest;
import com.skillsphere.nexus.skill_service.dto.response.SkillCompetencyResponse;
import com.skillsphere.nexus.skill_service.model.Employee;
import com.skillsphere.nexus.skill_service.model.Skill;
import com.skillsphere.nexus.skill_service.model.SkillCompetency;
import com.skillsphere.nexus.skill_service.repository.EmployeeRepository;
import com.skillsphere.nexus.skill_service.repository.SkillCompetencyRepository;
import com.skillsphere.nexus.skill_service.repository.SkillRepository;
import com.skillsphere.nexus.skill_service.service.SkillCompetencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkillCompetencyServiceImpl implements SkillCompetencyService {

    private final SkillCompetencyRepository competencyRepository;
    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;

    @Override
    public SkillCompetencyResponse addCompetency(SkillCompetencyRequest request) {

        if (competencyRepository.existsByEmployeeEmployeeIdAndSkillSkillId(
                request.getEmployeeId(),
                request.getSkillId())) {

            throw new RuntimeException("Employee already possesses this skill.");
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id : " + request.getEmployeeId()));

        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() ->
                        new RuntimeException("Skill not found with id : " + request.getSkillId()));

        SkillCompetency competency = SkillCompetency.builder()
                .employee(employee)
                .skill(skill)
                .proficiencyLevel(request.getProficiencyLevel())
                .yearsOfExperience(request.getYearsOfExperience())
                .lastUpdated(LocalDateTime.now())
                .build();

        return mapToResponse(competencyRepository.save(competency));
    }

    @Override
    public SkillCompetencyResponse updateCompetency(
            UUID competencyId,
            SkillCompetencyRequest request) {

        SkillCompetency competency = competencyRepository.findById(competencyId)
                .orElseThrow(() ->
                        new RuntimeException("Competency not found with id : " + competencyId));

        competency.setProficiencyLevel(request.getProficiencyLevel());
        competency.setYearsOfExperience(request.getYearsOfExperience());
        competency.setLastUpdated(LocalDateTime.now());

        return mapToResponse(competencyRepository.save(competency));
    }

    @Override
    public SkillCompetencyResponse getCompetencyById(UUID competencyId) {

        SkillCompetency competency = competencyRepository.findById(competencyId)
                .orElseThrow(() ->
                        new RuntimeException("Competency not found with id : " + competencyId));

        return mapToResponse(competency);
    }

    @Override
    public List<SkillCompetencyResponse> getAllCompetencies() {

        return competencyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteCompetency(UUID competencyId) {

        SkillCompetency competency = competencyRepository.findById(competencyId)
                .orElseThrow(() ->
                        new RuntimeException("Competency not found with id : " + competencyId));

        competencyRepository.delete(competency);
    }

    @Override
    public List<SkillCompetencyResponse> getCompetenciesByEmployee(UUID employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id : " + employeeId));

        return competencyRepository.findByEmployeeEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SkillCompetencyResponse> getCompetenciesBySkill(UUID skillId) {

        skillRepository.findById(skillId)
                .orElseThrow(() ->
                        new RuntimeException("Skill not found with id : " + skillId));

        return competencyRepository.findBySkillSkillId(skillId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SkillCompetencyResponse> getCompetenciesByProficiencyLevel(Integer proficiencyLevel) {

        return competencyRepository.findByProficiencyLevel(proficiencyLevel)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public double calculateAverageExperience(UUID employeeId) {

        List<SkillCompetency> competencies =
                competencyRepository.findByEmployeeEmployeeId(employeeId);

        return competencies.stream()
                .mapToInt(SkillCompetency::getYearsOfExperience)
                .average()
                .orElse(0.0);
    }

    @Override
    public boolean competencyExists(UUID employeeId, UUID skillId) {

        return competencyRepository.existsByEmployeeEmployeeIdAndSkillSkillId(
                employeeId,
                skillId);
    }

    // Helper Method

    private SkillCompetencyResponse mapToResponse(
            SkillCompetency competency) {

        return SkillCompetencyResponse.builder()
                .competencyId(competency.getCompetencyId())
                .employeeName(
                        competency.getEmployee().getFirstName()
                                + " "
                                + competency.getEmployee().getLastName())
                .skillName(competency.getSkill().getSkillName())
                .proficiencyLevel(competency.getProficiencyLevel())
                .yearsOfExperience(competency.getYearsOfExperience())
                .lastUpdated(competency.getLastUpdated())
                .build();
    }
}