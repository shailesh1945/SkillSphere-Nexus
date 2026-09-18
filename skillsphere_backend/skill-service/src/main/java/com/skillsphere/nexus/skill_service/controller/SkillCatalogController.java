package com.skillsphere.nexus.skill_service.controller;

import com.skillsphere.nexus.skill_service.dto.request.SkillRequest;
import com.skillsphere.nexus.skill_service.dto.response.SkillResponse;
import com.skillsphere.nexus.skill_service.service.SkillCatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/skills/catalog")
@RequiredArgsConstructor
public class SkillCatalogController {

    private final SkillCatalogService skillCatalogService;


    @PostMapping
    @PreAuthorize("hasAnyRole('HR', 'TRAINING_MANAGER', 'ADMIN')")
    public ResponseEntity<SkillResponse> addSkill(
            @Valid @RequestBody SkillRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(skillCatalogService.addSkill(request));
    }

    @PutMapping("/{skillId}")
    @PreAuthorize("hasAnyRole('HR', 'TRAINING_MANAGER', 'ADMIN')")
    public ResponseEntity<SkillResponse> updateSkill(
            @PathVariable UUID skillId,
            @Valid @RequestBody SkillRequest request) {

        return ResponseEntity.ok(
                skillCatalogService.updateSkill(
                        skillId,
                        request
                )
        );
    }

    @GetMapping("/{skillId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SkillResponse> getSkillById(
            @PathVariable UUID skillId) {

        return ResponseEntity.ok(
                skillCatalogService.getSkillById(skillId)
        );
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SkillResponse>> getAllSkills() {

        return ResponseEntity.ok(
                skillCatalogService.getAllSkills()
        );
    }

    @DeleteMapping("/{skillId}")
    @PreAuthorize("hasAnyRole('HR', 'TRAINING_MANAGER', 'ADMIN')")
    public ResponseEntity<Void> deleteSkill(
            @PathVariable UUID skillId) {

        skillCatalogService.deleteSkill(skillId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SkillResponse>>
    getSkillsByCategory(
            @PathVariable String category) {

        return ResponseEntity.ok(
                skillCatalogService.getSkillsByCategory(category)
        );
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SkillResponse>>
    searchSkills(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                skillCatalogService.searchSkills(keyword)
        );
    }

    @GetMapping("/exists")
    @PreAuthorize("hasAnyRole('HR', 'TRAINING_MANAGER', 'ADMIN')")
    public ResponseEntity<Boolean> skillExists(
            @RequestParam String skillName) {

        return ResponseEntity.ok(
                skillCatalogService.skillExists(skillName)
        );
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('HR', 'TRAINING_MANAGER', 'ADMIN')")
    public ResponseEntity<Long> getSkillCount() {

        return ResponseEntity.ok(
                skillCatalogService.getSkillCount()
        );
    }

}
