package com.skillsphere.nexus.service.impl;

import com.skillsphere.nexus.dto.request.EmployeeRequest;
import com.skillsphere.nexus.dto.response.EmployeeResponse;
import com.skillsphere.nexus.model.Employee;
import com.skillsphere.nexus.repository.EmployeeRepository;
import com.skillsphere.nexus.service.EmployeeService;
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
                .status("ACTIVE")
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

        employee.setStatus("ACTIVE");
        employee.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponse deactivateEmployee(UUID employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found with id: " + employeeId));

        employee.setStatus("INACTIVE");
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

        return employeeRepository.findByStatusIgnoreCase("ACTIVE")
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EmployeeResponse> getInactiveEmployees() {

        return employeeRepository.findByStatusIgnoreCase("INACTIVE")
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
                .status(employee.getStatus())
                .build();
    }
}