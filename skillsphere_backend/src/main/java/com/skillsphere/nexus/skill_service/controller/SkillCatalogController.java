package com.skillsphere.nexus.skill_service.controller;

import com.skillsphere.nexus.skill_service.dto.response.SkillResponse;
import com.skillsphere.nexus.skill_service.service.SkillCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/skills/catalog")
@RequiredArgsConstructor
public class SkillCatalogController {

    private final SkillCatalogService skillCatalogService;

    @GetMapping
    public List<SkillResponse> getCatalog(){
        return skillCatalogService.getAllSkills();
    }

    public SkillResponse addSkill(SkillResponse dto){
        return skillCatalogService.addSkill(dto);
    }
}
