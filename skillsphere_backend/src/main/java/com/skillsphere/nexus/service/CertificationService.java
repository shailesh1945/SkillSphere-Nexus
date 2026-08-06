package com.skillsphere.nexus.service;

import com.skillsphere.nexus.dto.request.CertificationRequest;
import com.skillsphere.nexus.dto.response.CertificationResponse;

import java.util.List;
import java.util.UUID;

public interface CertificationService {


    CertificationResponse addCertification(CertificationRequest request);

    CertificationResponse updateCertification(UUID certificationId,
                                              CertificationRequest request);

    CertificationResponse getCertificationById(UUID certificationId);

    List<CertificationResponse> getAllCertifications();

    void deleteCertification(UUID certificationId);

    // Enterprise Methods

    List<CertificationResponse> getEmployeeCertifications(UUID employeeId);

    List<CertificationResponse> getActiveCertifications();

    List<CertificationResponse> getExpiredCertifications();

    List<CertificationResponse> getCertificationsExpiringWithin(int days);

    CertificationResponse renewCertification(UUID certificationId,
                                             CertificationRequest request);

}