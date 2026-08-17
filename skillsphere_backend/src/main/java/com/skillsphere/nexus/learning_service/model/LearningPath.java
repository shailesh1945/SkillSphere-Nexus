package com.skillsphere.nexus.learning_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "learning_paths")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningPath {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pathId;

    @Column(nullable = false)
    private String name;

    private String description;

    private String careerTrack;

    private Integer progress;

    private Boolean active;
}