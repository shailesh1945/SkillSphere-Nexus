package com.skillsphere.nexus.skill_service.service.impl;

import com.skillsphere.nexus.skill_service.dto.request.AssessmentRequest;
import com.skillsphere.nexus.skill_service.dto.response.AssessmentResponse;
import com.skillsphere.nexus.skill_service.model.Assessment;
import com.skillsphere.nexus.skill_service.model.Employee;
import com.skillsphere.nexus.skill_service.model.Skill;
import com.skillsphere.nexus.skill_service.repository.AssessmentRepository;
import com.skillsphere.nexus.skill_service.repository.EmployeeRepository;
import com.skillsphere.nexus.skill_service.repository.SkillRepository;
import com.skillsphere.nexus.skill_service.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;


    @Override
    public AssessmentResponse addAssessment(AssessmentRequest request) {

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        boolean passed = request.getScore() >= 70.0;

        Assessment assessment = Assessment.builder()
                .assessmentName(request.getAssessmentName())
                .assessmentDate(request.getAssessmentDate())
                .score(request.getScore())
                .passed(passed)
                .result(passed ? "PASS" : "FAIL")
                .verified(false)
                .employee(employee)
                .skill(skill)
                .build();

        return mapToResponse(assessmentRepository.save(assessment));
    }


    @Override
    public AssessmentResponse updateAssessment(UUID assessmentId,
                                               AssessmentRequest request) {

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() ->
                        new RuntimeException("Skill not found"));

        boolean passed = request.getScore() >= 70.0;

        assessment.setAssessmentName(request.getAssessmentName());
        assessment.setAssessmentDate(request.getAssessmentDate());
        assessment.setScore(request.getScore());
        assessment.setPassed(passed);
        assessment.setResult(passed ? "PASS" : "FAIL");
        assessment.setEmployee(employee);
        assessment.setSkill(skill);
        assessment.setVerified(false);

        Assessment updatedAssessment = assessmentRepository.save(assessment);

        return mapToResponse(updatedAssessment);
    }

    @Override
    public AssessmentResponse getAssessmentById(UUID assessmentId) {

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() ->
                        new RuntimeException("Assessment not found"));

        return mapToResponse(assessment);
    }

    @Override
    public List<AssessmentResponse> getAllAssessments() {

        return assessmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteAssessment(UUID assessmentId) {

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() ->
                        new RuntimeException("Assessment not found"));

        assessmentRepository.delete(assessment);
    }

    @Override
    public List<AssessmentResponse> getAssessmentsByEmployee(UUID employeeId) {

        return assessmentRepository.findByEmployeeEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AssessmentResponse> getAssessmentsBySkill(UUID skillId) {

        return assessmentRepository.findBySkillSkillId(skillId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Double calculateAverageScore(UUID employeeId) {

        List<Assessment> assessments =
                assessmentRepository.findByEmployeeEmployeeId(employeeId);

        return assessments.stream()
                .mapToDouble(Assessment::getScore)
                .average()
                .orElse(0.0);
    }

    @Override
    public List<AssessmentResponse> getPassedAssessments() {

        return assessmentRepository.findByResultIgnoreCase("PASS")
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AssessmentResponse> getFailedAssessments() {

        return assessmentRepository.findByResultIgnoreCase("FAIL")
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AssessmentResponse verifyAssessment(UUID assessmentId) {

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        assessment.setVerified(true);
        assessmentRepository.save(assessment);

        return mapToResponse(assessment);

    }


    private AssessmentResponse mapToResponse(Assessment assessment) {

        return AssessmentResponse.builder()
                .assessmentId(
                        assessment.getAssessmentId())

                .assessmentName(
                        assessment.getAssessmentName())

                .assessmentDate(
                        assessment.getAssessmentDate())

                .employeeId(
                        assessment.getEmployee()
                                .getEmployeeId())
                .skillId(
                        assessment.getSkill()
                                .getSkillId())
                .score(
                        assessment.getScore())

                .passed(
                        assessment.getPassed())

                .result(
                        assessment.getResult())

                .verified(
                        assessment.getVerified())

                .build();
    }
}