package com.skillsphere.nexus.service;

import com.skillsphere.nexus.dto.request.EmployeeRequest;
import com.skillsphere.nexus.dto.response.EmployeeResponse;
import com.skillsphere.nexus.model.Employee;

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

    List<EmployeeResponse> getEmployeesByDesignation(String designation);

    List<EmployeeResponse> searchEmployees(String keyword);

    EmployeeResponse activateEmployee(UUID employeeId);

    EmployeeResponse deactivateEmployee(UUID employeeId);

    boolean emailExists(String email);

    long getEmployeeCount();

    List<EmployeeResponse> getActiveEmployees();

    List<EmployeeResponse> getInactiveEmployees();
}
