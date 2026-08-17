package com.skillsphere.nexus.skill_service.dto.request;


import com.skillsphere.nexus.skill_service.model.Employee;
import com.skillsphere.nexus.skill_service.model.Employee.Role;
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