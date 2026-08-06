package com.skillsphere.nexus.controller;


import com.skillsphere.nexus.dto.request.EmployeeRequest;
import com.skillsphere.nexus.dto.response.EmployeeResponse;
import com.skillsphere.nexus.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;


    @PostMapping
    public ResponseEntity<EmployeeResponse> addEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.addEmployee(request));
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable UUID employeeId,
            @Valid @RequestBody EmployeeRequest request) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(employeeId, request));
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable UUID employeeId) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(employeeId));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

        return ResponseEntity.ok(
                employeeService.getAllEmployees());
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable UUID employeeId) {

        employeeService.deleteEmployee(employeeId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<EmployeeResponse>> getByDepartment(
            @PathVariable String department) {

        return ResponseEntity.ok(
                employeeService.getEmployeesByDepartment(department));
    }

    @GetMapping("/designation/{designation}")
    public ResponseEntity<List<EmployeeResponse>> getByDesignation(
            @PathVariable String designation) {

        return ResponseEntity.ok(
                employeeService.getEmployeesByDesignation(designation));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponse>> searchEmployees(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                employeeService.searchEmployees(keyword));
    }

    @PatchMapping("/{employeeId}/activate")
    public ResponseEntity<Void> activateEmployee(
            @PathVariable UUID employeeId) {

        employeeService.activateEmployee(employeeId);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{employeeId}/deactivate")
    public ResponseEntity<Void> deactivateEmployee(
            @PathVariable UUID employeeId) {

        employeeService.deactivateEmployee(employeeId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<EmployeeResponse>> getActiveEmployees() {

        return ResponseEntity.ok(
                employeeService.getActiveEmployees());
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<EmployeeResponse>> getInactiveEmployees() {

        return ResponseEntity.ok(
                employeeService.getInactiveEmployees());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getEmployeeCount() {

        return ResponseEntity.ok(
                employeeService.getEmployeeCount());
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> emailExists(
            @RequestParam String email) {

        return ResponseEntity.ok(
                employeeService.emailExists(email));
    }

}
