package com.skillsphere.nexus.dto.request;


import com.skillsphere.nexus.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployeeRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

    private String department;

    private String designation;

    private Role role;
}