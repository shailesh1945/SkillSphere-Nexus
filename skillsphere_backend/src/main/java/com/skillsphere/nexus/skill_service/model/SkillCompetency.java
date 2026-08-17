package com.skillsphere.nexus.skill_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "skill_competencies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillCompetency {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID competencyId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false)
    private Integer proficiencyLevel;

    private Integer yearsOfExperience;

    private LocalDateTime lastUpdated;
}