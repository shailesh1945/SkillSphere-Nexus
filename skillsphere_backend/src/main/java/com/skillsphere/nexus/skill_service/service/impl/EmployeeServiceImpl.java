package com.skillsphere.nexus.skill_service.service.impl;

import com.skillsphere.nexus.skill_service.dto.request.EmployeeRequest;
import com.skillsphere.nexus.skill_service.dto.response.EmployeeResponse;
import com.skillsphere.nexus.skill_service.model.Employee;
import com.skillsphere.nexus.skill_service.repository.EmployeeRepository;
import com.skillsphere.nexus.skill_service.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponse addEmployee(EmployeeRequest request) {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Employee already exists with email: " + request.getEmail());
        }

        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .department(request.getDepartment())
                .designation(request.getDesignation())
                .role(request.getRole())
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return mapToResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponse updateEmployee(UUID employeeId,
                                           EmployeeRequest request) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + employeeId));

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setDesignation(request.getDesignation());
        employee.setRole(request.getRole());
        employee.setUpdatedAt(LocalDateTime.now());

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
    public List<EmployeeResponse> getEmployeesByDesignation(String designation) {

        return employeeRepository.findByDesignationIgnoreCase(designation)
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
    public EmployeeResponse activateEmployee(UUID employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + employeeId));

        employee.setActive(true);
        employee.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponse deactivateEmployee(UUID employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + employeeId));

        employee.setActive(false);
        employee.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(employeeRepository.save(employee));
    }

    @Override
    public boolean emailExists(String email) {
        return employeeRepository.existsByEmail(email);
    }

    @Override
    public long getEmployeeCount() {
        return employeeRepository.count();
    }

    @Override
    public List<EmployeeResponse> getActiveEmployees() {

        return employeeRepository.findByActive(true)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EmployeeResponse> getInactiveEmployees() {

        return employeeRepository.findByActive(false)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Helper method to map Employee entity to EmployeeResponse DTO

    private EmployeeResponse mapToResponse(Employee employee) {

        return EmployeeResponse.builder()
                .employeeId(employee.getEmployeeId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .designation(employee.getDesignation())
                .role(employee.getRole())
                .active(employee.getActive())
                .build();
    }
}