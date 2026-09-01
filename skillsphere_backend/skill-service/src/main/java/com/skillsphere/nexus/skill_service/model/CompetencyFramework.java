package com.skillsphere.nexus.skill_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "competency_framework")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetencyFramework {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID frameworkId;

    private String roleTitle;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private Integer requiredProficiency;
}