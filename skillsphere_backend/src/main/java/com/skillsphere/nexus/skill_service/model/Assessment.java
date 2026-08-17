package com.skillsphere.nexus.skill_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "assessments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID assessmentId;

    @Column(nullable = false)
    private String assessmentName;

    private LocalDate assessmentDate;

    private Double score;

    private Double maximumScore;

    private String result;

    private  Boolean verified;
//    private Boolean verified;

    private String remarks;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
}