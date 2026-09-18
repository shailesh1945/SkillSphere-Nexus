package com.skillsphere.nexus.skill_service.controller;


import com.skillsphere.nexus.skill_service.dto.request.EmployeeRequest;
import com.skillsphere.nexus.skill_service.dto.response.EmployeeResponse;
import com.skillsphere.nexus.skill_service.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;


    @PostMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<EmployeeResponse> addEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.addEmployee(request));
    }

    @PutMapping("/{employeeId}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable UUID employeeId,
            @Valid @RequestBody EmployeeRequest request) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(employeeId, request));
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable UUID employeeId) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(employeeId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

        return ResponseEntity.ok(
                employeeService.getAllEmployees());
    }

    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable UUID employeeId) {

        employeeService.deleteEmployee(employeeId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{department}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<List<EmployeeResponse>> getByDepartment(
            @PathVariable String department) {

        return ResponseEntity.ok(
                employeeService.getEmployeesByDepartment(department));
    }


    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<List<EmployeeResponse>> searchEmployees(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                employeeService.searchEmployees(keyword));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<Long> getEmployeeCount() {

        return ResponseEntity.ok(
                employeeService.getEmployeeCount());
    }

    @GetMapping("/exists")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public ResponseEntity<Boolean> emailExists(
            @RequestParam String email) {

        return ResponseEntity.ok(
                employeeService.emailExists(email));
    }

}
