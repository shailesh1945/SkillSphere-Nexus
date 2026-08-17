package com.skillsphere.nexus.skill_service.controller;

import com.skillsphere.nexus.skill_service.service.impl.SkillProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/skill-profiles")
@RequiredArgsConstructor
public class SkillProfileController {

    private final SkillProfileServiceImpl skillProfileService;

    @GetMapping("/{employeeId}")
    public ResponseEntity<Map<String, Object>> getEmployeeSkillProfile(@PathVariable UUID employeeId) {

        return ResponseEntity.ok(
                skillProfileService.buildSkillProfile(employeeId)
        );
    }
}
