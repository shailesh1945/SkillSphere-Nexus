package com.skillsphere.nexus.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillProfileResponse {

    private EmployeeResponse employee;

    private List<SkillCompetencyResponse> competencies;

    private List<AssessmentResponse> assessments;

    private List<CertificationResponse> certifications;

    private Integer totalSkills;

    private Integer totalCertifications;

    private Double averageAssessmentScore;
}