package com.skillsphere.nexus.skill_service.service;


import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakUserService {

    private final Keycloak keycloak;

    @Value("${keycloak.target.realm}")
    private String targetRealm;

    public String createEmployeeUser(
            String username,
            String email,
            String firstName,
            String lastName,
            String temporaryPassword) {

        UserRepresentation user = new UserRepresentation();

        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);
        user.setEmailVerified(false);

        CredentialRepresentation password =
                new CredentialRepresentation();

        password.setType(
                CredentialRepresentation.PASSWORD
        );

        password.setValue(temporaryPassword);

        password.setTemporary(true);

        user.setCredentials(
                List.of(password)
        );

        Response response = keycloak
                .realm(targetRealm)
                .users()
                .create(user);

        try {

            if (response.getStatus() != 201) {

                throw new RuntimeException(
                        "Failed to create Keycloak user. HTTP status: "
                                + response.getStatus()
                );
            }

            String userId =
                    extractUserId(response);

            assignEmployeeRole(userId);

            return userId;

        } finally {
            response.close();
        }
    }

    private String extractUserId(Response response) {

        String location =
                response.getHeaderString("Location");

        if (location == null || location.isBlank()) {
            throw new RuntimeException(
                    "Keycloak created the user but did not return the user ID."
            );
        }

        return location.substring(
                location.lastIndexOf('/') + 1
        );
    }

    private void assignEmployeeRole(String userId) {

        RoleResource employeeRole =
                keycloak
                        .realm(targetRealm)
                        .roles()
                        .get("ROLE_EMPLOYEE");

        RoleRepresentation role =
                employeeRole.toRepresentation();

        UserResource user =
                keycloak
                        .realm(targetRealm)
                        .users()
                        .get(userId);

        user.roles()
                .realmLevel()
                .add(
                        List.of(role)
                );
    }
}
