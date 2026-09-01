package com.skillsphere.nexus.skill_service.service.impl;

import com.skillsphere.nexus.skill_service.model.Assessment;
import com.skillsphere.nexus.skill_service.model.Certification;
import com.skillsphere.nexus.skill_service.model.Employee;
import com.skillsphere.nexus.skill_service.model.EmployeeSkill;
import com.skillsphere.nexus.skill_service.repository.*;
import com.skillsphere.nexus.skill_service.service.SkillProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SkillProfileServiceImpl implements SkillProfileService {

    private final EmployeeRepository employeeRepository;
    private final CompetencyFrameworkRepository competencyRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final AssessmentRepository assessmentRepository;
    private final CertificationRepository certificationRepository;


    @Cacheable(value = "employeeSkillProfile", key = "#employeeId")
    public Map<String, Object> getSkillProfile(UUID employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id : " + employeeId));

        List<EmployeeSkill> skills =
                employeeSkillRepository.findByEmployeeEmployeeId(employeeId);

        List<Assessment> assessments =
                assessmentRepository.findByEmployeeEmployeeId(employeeId);

        List<Certification> certifications =
                certificationRepository.findByEmployeeEmployeeId(employeeId);

        Map<String, Object> profile = new HashMap<>();
        profile.put("employeeDetails", employee);
        profile.put("skills", skills);
        profile.put("skillAssessments", assessments);
        profile.put("employeeCertifications", certifications);
        profile.put("totalSkills", skills.size());
        profile.put("totalCertifications", certifications.size());

        return profile;
    }


}