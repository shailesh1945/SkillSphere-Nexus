package com.skillsphere.nexus.skill_service.service.impl;

import com.skillsphere.nexus.skill_service.dto.request.EmployeeRequest;
import com.skillsphere.nexus.skill_service.dto.response.EmployeeResponse;
import com.skillsphere.nexus.skill_service.model.Employee;
import com.skillsphere.nexus.skill_service.repository.EmployeeRepository;
import com.skillsphere.nexus.skill_service.service.CurrentEmployeeService;
import com.skillsphere.nexus.skill_service.service.EmployeeService;
import com.skillsphere.nexus.skill_service.service.KeycloakUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final CurrentEmployeeService currentEmployeeService;

    private final KeycloakUserService keycloakUserService;

    @Override
    @Transactional
    public EmployeeResponse addEmployee(EmployeeRequest request) {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Employee with this email already exists."
            );
        }

        String keycloakUserId =
                keycloakUserService.createEmployeeUser(
                        request.getUsername(),
                        request.getEmail(),
                        request.getFirstName(),
                        request.getLastName(),
                        request.getTemporaryPassword()
                );

        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .keycloakUserId(keycloakUserId)
                .phoneNumber(request.getPhoneNumber())
                .department(request.getDepartment())
                .joiningDate(request.getJoiningDate())
                .role(
                        Employee.Role.valueOf(
                                request.getRole()
                        )
                )
                .build();

        Employee savedEmployee =
                employeeRepository.save(employee);

        return mapToResponse(savedEmployee);
    }

    @Override
    public EmployeeResponse updateEmployee(UUID employeeId,
                                           EmployeeRequest request) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + employeeId));

        // Prevent duplicate email
        if (!employee.getEmail().equalsIgnoreCase(request.getEmail())
                && employeeRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException(
                    "Employee already exists with email: "
                            + request.getEmail()
            );
        }

        employee.setFirstName(
                request.getFirstName()
        );

        employee.setLastName(
                request.getLastName()
        );

        employee.setEmail(
                request.getEmail()
        );

        employee.setPhoneNumber(
                request.getPhoneNumber()
        );

        employee.setDepartment(
                request.getDepartment()
        );

        employee.setJoiningDate(
                request.getJoiningDate()
        );

        employee.setRole(
                parseRole(request.getRole())
        );

        return mapToResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponse getEmployeeById(UUID employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + employeeId));

        return mapToResponse(employee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteEmployee(UUID employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + employeeId));

        employeeRepository.delete(employee);
    }

    @Override
    public List<EmployeeResponse> getEmployeesByDepartment(String department) {

        return employeeRepository.findByDepartmentIgnoreCase(department)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public List<EmployeeResponse> searchEmployees(String keyword) {

        return employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }




    @Override
    public boolean emailExists(String email) {
        return employeeRepository.existsByEmail(email);
    }

    @Override
    public long getEmployeeCount() {
        return employeeRepository.count();
    }

    // method to parse role from string to Employee.Role enum
    private Employee.Role parseRole(String role) {

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException(
                    "Employee role is required"
            );
        }

        try {
            return Employee.Role.valueOf(
                    role.trim().toUpperCase()
            );

        } catch (IllegalArgumentException exception) {

            throw new IllegalArgumentException(
                    "Invalid employee role: " + role
            );
        }
    }

    @Override
    public EmployeeResponse getCurrentEmployee(
            Authentication authentication) {

        Employee employee =
                currentEmployeeService
                        .getCurrentEmployee(authentication);

        return mapToResponse(employee);
    }


    // Helper method to map Employee entity to EmployeeResponse DTO

    private EmployeeResponse mapToResponse(Employee employee) {

        return EmployeeResponse.builder()
                .employeeId(
                        employee.getEmployeeId()
                )
                .firstName(
                        employee.getFirstName()
                )
                .lastName(
                        employee.getLastName()
                )
                .email(
                        employee.getEmail()
                )
                .phoneNumber(
                        employee.getPhoneNumber()
                )
                .keycloakUserId(
                        employee.getKeycloakUserId()
                )

                .department(
                        employee.getDepartment()
                )
                .joiningDate(
                        employee.getJoiningDate()
                )
                .role(
                        employee.getRole() != null
                                ? employee.getRole().name()
                                : null
                )
                .build();
    }
}