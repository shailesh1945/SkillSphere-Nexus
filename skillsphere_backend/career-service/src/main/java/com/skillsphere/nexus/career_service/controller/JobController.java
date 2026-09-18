package com.skillsphere.nexus.career_service.controller;

import com.skillsphere.nexus.career_service.dto.request.JobRequest;
import com.skillsphere.nexus.career_service.dto.response.JobResponse;
import com.skillsphere.nexus.career_service.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/career/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;


    @PostMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public JobResponse createJob(
            @RequestBody JobRequest request) {

        return jobService.createJob(request);
    }


    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<JobResponse> getAllJobs() {

        return jobService.getAllJobs();
    }


    @GetMapping("/active")
    @PreAuthorize("isAuthenticated()")
    public List<JobResponse> getActiveJobs() {

        return jobService.getActiveJobs();
    }


    @GetMapping("/department/{department}")
    @PreAuthorize("isAuthenticated()")
    public List<JobResponse> getJobsByDepartment(
            @PathVariable String department) {

        return jobService
                .getJobsByDepartment(department);
    }


    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public void deleteJob(
            @PathVariable UUID jobId) {

        jobService.deleteJob(jobId);
    }
}