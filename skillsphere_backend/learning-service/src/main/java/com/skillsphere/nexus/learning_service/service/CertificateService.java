package com.skillsphere.nexus.learning_service.service;

import com.skillsphere.nexus.learning_service.model.LearningCertificate;

import java.util.UUID;

public interface CertificateService {

    LearningCertificate generateCertificate(UUID enrollmentId);


}
