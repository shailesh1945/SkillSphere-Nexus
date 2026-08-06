package com.skillsphere.nexus.service.impl;

import com.skillsphere.nexus.dto.response.SkillResponse;
import com.skillsphere.nexus.enums.Category;
import com.skillsphere.nexus.model.Skill;
import com.skillsphere.nexus.repository.SkillRepository;
import com.skillsphere.nexus.service.SkillCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillCatalogServiceImpl implements SkillCatalogService {
    private final SkillRepository skillRepository;
    public List<SkillResponse> getAllSkills() {
        return skillRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public SkillResponse addSkill(SkillResponse dto) {

        Skill skill = Skill.builder()
                .skillName(dto.getSkillName())
                .category(Category.valueOf(dto.getCategory()))
                .build();
        return toDTO(skillRepository.save(skill));
    }


    private SkillResponse toDTO(Skill skill) {
        return SkillResponse.builder()
                .skillId(skill.getSkillId())
                .skillName(skill.getSkillName())
                .category(skill.getCategory().name())
                .build();
    }
}
