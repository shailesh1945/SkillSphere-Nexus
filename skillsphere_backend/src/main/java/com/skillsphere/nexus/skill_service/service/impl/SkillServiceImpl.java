package com.skillsphere.nexus.skill_service.service.impl;

import com.skillsphere.nexus.skill_service.dto.request.SkillRequest;
import com.skillsphere.nexus.skill_service.dto.response.SkillResponse;
import com.skillsphere.nexus.skill_service.model.Skill;
import com.skillsphere.nexus.skill_service.model.Skill.Category;
import com.skillsphere.nexus.skill_service.repository.SkillRepository;
import com.skillsphere.nexus.skill_service.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Override
    public SkillResponse addSkill(SkillRequest request) {

        if (skillRepository.existsBySkillName(request.getSkillName())) {
            throw new RuntimeException("Skill already exists: " + request.getSkillName());
        }

        Skill skill = Skill.builder()
                .skillName(request.getSkillName())
                .description(request.getDescription())
                .category(Category.valueOf(request.getCategory()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return mapToResponse(skillRepository.save(skill));
    }

    @Override
    public SkillResponse updateSkill(UUID skillId, SkillRequest request) {

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() ->
                        new RuntimeException("Skill not found with id: " + skillId));

        skill.setSkillName(request.getSkillName());
        skill.setDescription(request.getDescription());
        skill.setCategory(Category.valueOf(request.getCategory()));
        skill.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(skillRepository.save(skill));
    }

    @Override
    public SkillResponse getSkillById(UUID skillId) {

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() ->
                        new RuntimeException("Skill not found with id: " + skillId));

        return mapToResponse(skill);
    }

    @Override
    public List<SkillResponse> getAllSkills() {

        return skillRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteSkill(UUID skillId) {

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() ->
                        new RuntimeException("Skill not found with id: " + skillId));

        skillRepository.delete(skill);
    }

    @Override
    public List<SkillResponse> getSkillsByCategory(String category) {

        return skillRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<SkillResponse> searchSkills(String keyword) {

        return skillRepository.findBySkillNameContainingIgnoreCase(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public boolean skillExists(String skillName) {
        return skillRepository.existsBySkillName(skillName);
    }

    @Override
    public long getSkillCount() {
        return skillRepository.count();
    }

    // Helper method to map Skill entity to SkillResponse DTO
    private SkillResponse mapToResponse(Skill skill) {

        return SkillResponse.builder()
                .skillId(skill.getSkillId())
                .skillName(skill.getSkillName())
                .description(skill.getDescription())
                .category(skill.getCategory().name())
                .build();
    }
}