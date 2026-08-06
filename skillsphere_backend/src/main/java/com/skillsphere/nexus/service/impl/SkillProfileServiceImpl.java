package com.skillsphere.nexus.service.impl;

import com.skillsphere.nexus.model.Assessment;
import com.skillsphere.nexus.model.Certification;
import com.skillsphere.nexus.model.Employee;
import com.skillsphere.nexus.model.SkillCompetency;
import com.skillsphere.nexus.repository.AssessmentRepository;
import com.skillsphere.nexus.repository.CertificationRepository;
import com.skillsphere.nexus.repository.EmployeeRepository;
import com.skillsphere.nexus.repository.SkillCompetencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SkillProfileServiceImpl {

    private final EmployeeRepository employeeRepository;
    private final SkillCompetencyRepository skillCompetencyRepository;
    private final AssessmentRepository assessmentRepository;
    private final CertificationRepository certificationRepository;


    @Cacheable(value = "employeeSkillProfile", key = "#employeeId")
    public Map<String, Object> buildSkillProfile(UUID employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id : " + employeeId));

        List<SkillCompetency> competencies =
                skillCompetencyRepository.findByEmployeeEmployeeId(employeeId);

        List<Assessment> assessments =
                assessmentRepository.findByEmployeeEmployeeId(employeeId);

        List<Certification> certifications =
                certificationRepository.findByEmployeeEmployeeId(employeeId);

        Map<String, Object> profile = new HashMap<>();
        profile.put("employeeDetails", employee);
        profile.put("skillCompetencies", competencies);
        profile.put("skillAssessments", assessments);
        profile.put("employeeCertifications", certifications);
        profile.put("totalSkills", competencies.size());
        profile.put("totalCertifications", certifications.size());

        return profile;
    }


}