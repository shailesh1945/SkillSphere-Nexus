package com.skillsphere.nexus.skill_service.controller;

import com.skillsphere.nexus.skill_service.dto.request.AssessmentRequest;
import com.skillsphere.nexus.skill_service.dto.response.AssessmentResponse;
import com.skillsphere.nexus.skill_service.service.AssessmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    // create assessment
    @PostMapping
    public AssessmentResponse createAssessment(@RequestBody AssessmentRequest dto) {
        return assessmentService.addAssessment(dto);
    }

    // update assessment
    @PutMapping("/{id}")
    public AssessmentResponse updateAssessment(
            @PathVariable UUID id,
            @Valid @RequestBody AssessmentRequest request) {

        return assessmentService.updateAssessment(id, request);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public AssessmentResponse getAssessmentById(
            @PathVariable UUID id) {

        return assessmentService.getAssessmentById(id);
    }

    // GET ALL
    @GetMapping
    public List<AssessmentResponse> getAllAssessments() {

        return assessmentService.getAllAssessments();
    }



    // GET BY EMPLOYEE
    @GetMapping("/employee/{employeeId}")
    public List<AssessmentResponse> getAssessmentsByEmployee(
            @PathVariable UUID employeeId) {

        return assessmentService.getAssessmentsByEmployee(employeeId);
    }


    // GET BY SKILL
    @GetMapping("/skill/{skillId}")
    public List<AssessmentResponse> getAssessmentsBySkill(
            @PathVariable UUID skillId) {

        return assessmentService.getAssessmentsBySkill(skillId);
    }


    // GET PASSED

    @GetMapping("/passed")
    public List<AssessmentResponse> getPassedAssessments() {

        return assessmentService.getPassedAssessments();
    }


    // GET FAILED
    @GetMapping("/failed")
    public List<AssessmentResponse> getFailedAssessments() {

        return assessmentService.getFailedAssessments();
    }


    // =========================================================
    // VERIFY - HR ONLY
    // =========================================================

    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('HR')")
    public AssessmentResponse verifyAssessment(
            @PathVariable UUID id) {

        return assessmentService.verifyAssessment(id);
    }


    // DELETE
    @DeleteMapping("/{id}")
    public void deleteAssessment(
            @PathVariable UUID id) {

        assessmentService.deleteAssessment(id);
    }

}

