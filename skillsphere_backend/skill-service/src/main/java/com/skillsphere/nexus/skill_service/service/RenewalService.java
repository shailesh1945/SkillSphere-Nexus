package com.skillsphere.nexus.skill_service.service;

import com.skillsphere.nexus.skill_service.dto.request.RenewalRequest;
import com.skillsphere.nexus.skill_service.dto.response.RenewalResponse;

import java.util.UUID;

public interface RenewalService {

    RenewalResponse requestRenewal(
            UUID certificationId,
            String requestedBy);

    RenewalResponse approveRenewal(
            UUID renewalId,
            RenewalRequest request);
}
