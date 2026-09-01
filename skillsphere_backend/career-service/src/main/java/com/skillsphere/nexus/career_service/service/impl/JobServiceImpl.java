package com.skillsphere.nexus.career_service.service.impl;

import com.skillsphere.nexus.career_service.dto.request.JobRequest;
import com.skillsphere.nexus.career_service.dto.response.JobResponse;
import com.skillsphere.nexus.career_service.model.Job;
import com.skillsphere.nexus.career_service.repository.JobRepository;
import com.skillsphere.nexus.career_service.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobServiceImpl
        implements JobService {

    private final JobRepository jobRepository;


    @Override
    public JobResponse createJob(
            JobRequest request) {

        Job job = Job.builder()
                .title(request.getTitle())
                .department(request.getDepartment())
                .requiredSkills(
                        request.getRequiredSkills())
                .minimumExperience(
                        request.getMinimumExperience())
                .active(true)
                .build();

        Job savedJob =
                jobRepository.save(job);

        return mapToResponse(savedJob);
    }


    @Override
    public List<JobResponse> getAllJobs() {

        return jobRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public List<JobResponse> getActiveJobs() {

        return jobRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public List<JobResponse> getJobsByDepartment(
            String department) {

        return jobRepository
                .findByDepartmentIgnoreCase(department)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public void deleteJob(UUID jobId) {

        if (!jobRepository.existsById(jobId)) {

            throw new RuntimeException(
                    "Job not found with id: "
                            + jobId);
        }

        jobRepository.deleteById(jobId);
    }


    private JobResponse mapToResponse(
            Job job) {

        return JobResponse.builder()
                .jobId(job.getJobId())
                .title(job.getTitle())
                .department(job.getDepartment())
                .requiredSkills(
                        job.getRequiredSkills())
                .minimumExperience(
                        job.getMinimumExperience())
                .active(job.getActive())
                .build();
    }
}