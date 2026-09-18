package com.skillsphere.nexus.career_service.controller;

import com.skillsphere.nexus.career_service.dto.response.AnalyticsResponse;
import com.skillsphere.nexus.career_service.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/career/analytics")
@RequiredArgsConstructor

public class AnalyticsController {

    private final AnalyticsService analyticsService;


    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public AnalyticsResponse getAnalytics() {

        return analyticsService.getAnalytics();
    }
}