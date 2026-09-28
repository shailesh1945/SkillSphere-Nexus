package com.skillsphere.nexus.skill_service.service;

import com.skillsphere.nexus.skill_service.model.Employee;
import com.skillsphere.nexus.skill_service.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentEmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee getCurrentEmployee(
            Authentication authentication) {

        if (authentication == null) {
            throw new RuntimeException(
                    "Authentication is required"
            );
        }

        Object principal =
                authentication.getPrincipal();

        if (!(principal instanceof Jwt jwt)) {
            throw new RuntimeException(
                    "Authenticated user is not a JWT"
            );
        }

        String keycloakUserId =
                jwt.getSubject();

        if (keycloakUserId == null ||
                keycloakUserId.isBlank()) {

            throw new RuntimeException(
                    "Keycloak user ID not found in token"
            );
        }

        return employeeRepository
                .findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee profile not found for Keycloak user: "
                                        + keycloakUserId
                        )
                );
    }
}