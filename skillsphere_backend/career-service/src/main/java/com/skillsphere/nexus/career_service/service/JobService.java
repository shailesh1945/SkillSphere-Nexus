package com.skillsphere.nexus.career_service.service;

import com.skillsphere.nexus.career_service.dto.request.JobRequest;
import com.skillsphere.nexus.career_service.dto.response.JobResponse;

import java.util.List;
import java.util.UUID;

public interface JobService {

    JobResponse createJob(JobRequest request);

    List<JobResponse> getAllJobs();

    List<JobResponse> getActiveJobs();

    List<JobResponse> getJobsByDepartment(
            String department);

    void deleteJob(UUID jobId);
}