package com.skillsphere.nexus.skill_service.service;

import com.skillsphere.nexus.skill_service.dto.request.EmployeeRequest;
import com.skillsphere.nexus.skill_service.dto.response.EmployeeResponse;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {

    EmployeeResponse addEmployee(EmployeeRequest request);

    EmployeeResponse updateEmployee(UUID employeeId,
                                    EmployeeRequest request);

    EmployeeResponse getEmployeeById(UUID employeeId);

    List<EmployeeResponse> getAllEmployees();

    void deleteEmployee(UUID employeeId);

    List<EmployeeResponse> getEmployeesByDepartment(String department);

    List<EmployeeResponse> searchEmployees(String keyword);

    boolean emailExists(String email);

    long getEmployeeCount();

}
