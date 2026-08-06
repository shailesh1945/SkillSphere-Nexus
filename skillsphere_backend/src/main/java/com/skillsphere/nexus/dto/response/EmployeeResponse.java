package com.skillsphere.nexus.dto.response;

import com.skillsphere.nexus.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {

    private UUID employeeId;

    private String firstName;

    private String lastName;

    private String email;

    private String department;

    private String designation;

    private String status;

    private Role role;
}
