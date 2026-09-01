package com.skillsphere.nexus.skill_service.service.impl;



import com.skillsphere.nexus.skill_service.dto.response.ComplianceResponse;
import com.skillsphere.nexus.skill_service.model.Certification;
import com.skillsphere.nexus.skill_service.model.Employee;
import com.skillsphere.nexus.skill_service.repository.CertificationRepository;
import com.skillsphere.nexus.skill_service.repository.EmployeeRepository;
import com.skillsphere.nexus.skill_service.service.ComplianceService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComplianceServiceImpl implements ComplianceService {

    private final EmployeeRepository employeeRepository;

    private final CertificationRepository
            certificationRepository;

    @Override
    public ComplianceResponse getCompliance(
            UUID employeeId) {

        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"));


        List<Certification> certifications =
                certificationRepository
                        .findByEmployeeEmployeeId(
                                employeeId);


        long total =
                certifications.size();


        long expired =
                certifications.stream()
                        .filter(cert ->
                                cert.getStatus()
                                        == Certification.Status.EXPIRED)
                        .count();


        long valid =
                certifications.stream()
                        .filter(cert ->
                                cert.getStatus()
                                        == Certification.Status.VALID)
                        .count();


        return ComplianceResponse.builder()
                .employeeName(
                        employee.getFirstName()
                                + " "
                                + employee.getLastName())
                .totalCertifications(total)
                .validCertifications(valid)
                .expiredCertifications(expired)
                .compliant(
                        total > 0 && expired == 0)
                .build();
    }
}
