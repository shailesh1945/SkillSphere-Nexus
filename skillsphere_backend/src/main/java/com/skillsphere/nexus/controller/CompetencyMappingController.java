package com.skillsphere.nexus.controller;

import com.skillsphere.nexus.dto.response.CompetencyGapResponse;
import com.skillsphere.nexus.service.CompetencyMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/competency")
@RequiredArgsConstructor
public class CompetencyMappingController {

    private final CompetencyMappingService competencyMappingService;

    @GetMapping("/gaps")
    public List<CompetencyGapResponse> getCompetencyGap(
            @RequestParam UUID employeeId,
            @RequestParam String roleTitle) {

        return competencyMappingService
                .getCompetencyGap(employeeId, roleTitle);
    }
}