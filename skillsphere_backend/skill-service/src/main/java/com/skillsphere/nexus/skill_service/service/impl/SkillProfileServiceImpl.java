package com.skillsphere.nexus.skill_service.service.impl;

import com.skillsphere.nexus.skill_service.dto.request.EmployeeSkillRequest;
import com.skillsphere.nexus.skill_service.model.*;
import com.skillsphere.nexus.skill_service.repository.*;
import com.skillsphere.nexus.skill_service.service.SkillProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SkillProfileServiceImpl implements SkillProfileService {

    private final EmployeeRepository employeeRepository;
    private final CompetencyFrameworkRepository competencyRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final AssessmentRepository assessmentRepository;
    private final CertificationRepository certificationRepository;
    private final SkillRepository skillRepository;


    @Cacheable(value = "employeeSkillProfile", key = "#employeeId")
    @Override
    public Map<String, Object> getSkillProfile(UUID employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found with id: " + employeeId
                        ));

        List<EmployeeSkill> skills =
                employeeSkillRepository
                        .findByEmployeeEmployeeId(employeeId);

        List<Assessment> assessments =
                assessmentRepository
                        .findByEmployeeEmployeeId(employeeId);

        List<Certification> certifications =
                certificationRepository
                        .findByEmployeeEmployeeId(employeeId);


        Map<String, Object> profile = new HashMap<>();


        // =========================
        // Employee Details
        // =========================

        Map<String, Object> employeeDetails =
                new HashMap<>();

        employeeDetails.put(
                "employeeId",
                employee.getEmployeeId()
        );

        employeeDetails.put(
                "firstName",
                employee.getFirstName()
        );

        employeeDetails.put(
                "lastName",
                employee.getLastName()
        );

        employeeDetails.put(
                "email",
                employee.getEmail()
        );

        employeeDetails.put(
                "department",
                employee.getDepartment()
        );

        employeeDetails.put(
                "role",
                employee.getRole()
        );

        profile.put(
                "employeeDetails",
                employeeDetails
        );


        // =========================
        // Employee Skills
        // =========================

        List<Map<String, Object>> skillList =
                skills.stream()
                        .map(employeeSkill -> {

                            Map<String, Object> skillData =
                                    new HashMap<>();

                            skillData.put(
                                    "id",
                                    employeeSkill.getId()
                            );

                            skillData.put(
                                    "skillId",
                                    employeeSkill.getSkill().getSkillId()
                            );

                            skillData.put(
                                    "skillName",
                                    employeeSkill.getSkill().getSkillName()
                            );

                            skillData.put(
                                    "description",
                                    employeeSkill.getSkill().getDescription()
                            );

                            skillData.put(
                                    "category",
                                    employeeSkill.getSkill().getCategory()
                            );

                            skillData.put(
                                    "proficiency",
                                    employeeSkill.getProficiency()
                            );

                            return skillData;
                        })
                        .toList();

        profile.put(
                "skills",
                skillList
        );


        // =========================
        // Skill Assessments
        // =========================

        List<Map<String, Object>> assessmentList =
                assessments.stream()
                        .map(assessment -> {

                            Map<String, Object> assessmentData =
                                    new HashMap<>();

                            assessmentData.put(
                                    "assessmentId",
                                    assessment.getAssessmentId()
                            );

                            assessmentData.put(
                                    "assessmentName",
                                    assessment.getAssessmentName()
                            );

                            assessmentData.put(
                                    "assessmentDate",
                                    assessment.getAssessmentDate()
                            );

                            assessmentData.put(
                                    "skillId",
                                    assessment.getSkill().getSkillId()
                            );

                            assessmentData.put(
                                    "skillName",
                                    assessment.getSkill().getSkillName()
                            );

                            assessmentData.put(
                                    "score",
                                    assessment.getScore()
                            );

                            assessmentData.put(
                                    "passed",
                                    assessment.getPassed()
                            );

                            assessmentData.put(
                                    "result",
                                    assessment.getResult()
                            );

                            assessmentData.put(
                                    "verified",
                                    assessment.getVerified()
                            );

                            return assessmentData;
                        })
                        .toList();

        profile.put(
                "skillAssessments",
                assessmentList
        );


        // =========================
        // Certifications
        // =========================

        List<Map<String, Object>> certificationList =
                certifications.stream()
                        .map(certification -> {

                            Map<String, Object> certificationData =
                                    new HashMap<>();

                            certificationData.put(
                                    "certificationId",
                                    certification.getCertificationId()
                            );

                            certificationData.put(
                                    "certificationName",
                                    certification.getCertificationName()
                            );

                            certificationData.put(
                                    "issuingOrganization",
                                    certification.getIssuingOrganization()
                            );

                            certificationData.put(
                                    "issueDate",
                                    certification.getIssueDate()
                            );

                            certificationData.put(
                                    "expiryDate",
                                    certification.getExpiryDate()
                            );

                            certificationData.put(
                                    "credentialId",
                                    certification.getCredentialId()
                            );

                            certificationData.put(
                                    "credentialUrl",
                                    certification.getCredentialUrl()
                            );

                            certificationData.put(
                                    "status",
                                    certification.getStatus()
                            );

                            return certificationData;
                        })
                        .toList();

        profile.put(
                "employeeCertifications",
                certificationList
        );


        // =========================
        // Summary
        // =========================

        profile.put(
                "totalSkills",
                skillList.size()
        );

        profile.put(
                "totalCertifications",
                certificationList.size()
        );


        return profile;
    }

    @CacheEvict(
            value = "employeeSkillProfile",
            key = "#employeeId"
    )

    @Override
    public Map<String, Object> addSkillToProfile(
            UUID employeeId,
            EmployeeSkillRequest request) {

        // Make sure employee exists
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found with id: " + employeeId
                        ));

        // Make sure skill exists in organizational catalog
        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Skill not found with id: " + request.getSkillId()
                        ));

        // Prevent duplicate skill
        boolean alreadyExists =
                employeeSkillRepository
                        .existsByEmployeeEmployeeIdAndSkillSkillId(
                                employeeId,
                                request.getSkillId()
                        );

        if (alreadyExists) {
            throw new RuntimeException(
                    "This skill is already present in your profile."
            );
        }

        // Create employee-skill relationship
        EmployeeSkill employeeSkill = EmployeeSkill.builder()
                .employee(employee)
                .skill(skill)
                .proficiency(request.getProficiency())
                .build();

        employeeSkillRepository.save(employeeSkill);

        // Return updated profile
        return getSkillProfile(employeeId);
    }


}