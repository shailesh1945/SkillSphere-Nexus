package com.skillsphere.nexus.skill_service.service;

import com.skillsphere.nexus.skill_service.dto.request.CertificationRequest;
import com.skillsphere.nexus.skill_service.dto.response.CertificationResponse;

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

//    CertificationResponse renewCertification(UUID certificationId,
//                                             CertificationRequest request);

}