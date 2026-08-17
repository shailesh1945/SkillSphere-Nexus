package com.skillsphere.nexus.skill_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID skillId;

    @Column(nullable = false, unique = true)
    private String skillName;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SkillCompetency> skillCompetencies = new ArrayList<>();

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Assessment> assessments = new ArrayList<>();

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CompetencyFramework> competencyFrameworks = new ArrayList<>();

    public enum Category {
        TECHNICAL,
        SOFT_SKILL,
        MANAGEMENT
    }

}
