package com.skillsphere.nexus.skill_service.service.impl;

import com.skillsphere.nexus.skill_service.dto.request.SkillRequest;
import com.skillsphere.nexus.skill_service.dto.response.SkillResponse;
import com.skillsphere.nexus.skill_service.model.Skill;
import com.skillsphere.nexus.skill_service.model.Skill.Category;
import com.skillsphere.nexus.skill_service.repository.SkillRepository;
import com.skillsphere.nexus.skill_service.service.SkillCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkillCatalogServiceImpl implements SkillCatalogService {

    private final SkillRepository skillRepository;


    // =========================================================
    // CREATE
    // =========================================================

    @Override
    public SkillResponse addSkill(SkillRequest request) {

        if (skillRepository.existsBySkillName(request.getSkillName())) {
            throw new RuntimeException(
                    "Skill already exists: "
                            + request.getSkillName()
            );
        }

        Skill skill = Skill.builder()
                .skillName(request.getSkillName())
                .description(request.getDescription())
                .category(parseCategory(request.getCategory()))
                .build();

        return mapToResponse(
                skillRepository.save(skill)
        );
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    public SkillResponse updateSkill(
            UUID skillId,
            SkillRequest request) {

        Skill skill =
                skillRepository.findById(skillId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Skill not found with id: "
                                                + skillId
                                ));

        // Prevent duplicate skill names
        if (!skill.getSkillName().equalsIgnoreCase(
                request.getSkillName())
                && skillRepository.existsBySkillName(
                request.getSkillName())) {

            throw new RuntimeException(
                    "Skill already exists: "
                            + request.getSkillName()
            );
        }

        skill.setSkillName(
                request.getSkillName()
        );

        skill.setDescription(
                request.getDescription()
        );

        skill.setCategory(
                parseCategory(request.getCategory())
        );

        return mapToResponse(
                skillRepository.save(skill)
        );
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @Override
    public SkillResponse getSkillById(UUID skillId) {

        Skill skill =
                skillRepository.findById(skillId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Skill not found with id: "
                                                + skillId
                                ));

        return mapToResponse(skill);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    public List<SkillResponse> getAllSkills() {

        return skillRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // DELETE
    // =========================================================

    @Override
    public void deleteSkill(UUID skillId) {

        Skill skill =
                skillRepository.findById(skillId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Skill not found with id: "
                                                + skillId
                                ));

        skillRepository.delete(skill);
    }


    // =========================================================
    // GET BY CATEGORY
    // =========================================================

    @Override
    public List<SkillResponse> getSkillsByCategory(
            String category) {

        return skillRepository
                .findByCategory(Category.valueOf(category.toUpperCase()))
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // SEARCH
    // =========================================================

    @Override
    public List<SkillResponse> searchSkills(
            String keyword) {

        return skillRepository
                .findBySkillNameContainingIgnoreCase(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // CHECK EXISTS
    // =========================================================

    @Override
    public boolean skillExists(
            String skillName) {

        return skillRepository.existsBySkillName(skillName);
    }


    // =========================================================
    // COUNT
    // =========================================================

    @Override
    public long getSkillCount() {

        return skillRepository.count();
    }


    // =========================================================
    // HELPER - CATEGORY
    // =========================================================

    private Category parseCategory(String category) {

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException(
                    "Skill category is required"
            );
        }

        try {
            return Category.valueOf(
                    category.trim().toUpperCase()
            );

        } catch (IllegalArgumentException exception) {

            throw new IllegalArgumentException(
                    "Invalid skill category: " + category
            );
        }
    }


    // =========================================================
    // HELPER - ENTITY → RESPONSE
    // =========================================================

    private SkillResponse mapToResponse(
            Skill skill) {

        return SkillResponse.builder()
                .skillId(skill.getSkillId())
                .skillName(skill.getSkillName())
                .description(skill.getDescription())
                .category(
                        skill.getCategory() != null
                                ? skill.getCategory().name()
                                : null
                )
                .build();
    }
}
