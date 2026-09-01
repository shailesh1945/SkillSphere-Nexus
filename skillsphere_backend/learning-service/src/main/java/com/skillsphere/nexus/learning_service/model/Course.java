package com.skillsphere.nexus.learning_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID courseId;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    private Integer duration;

    @Enumerated(EnumType.STRING)
    private CourseType type;

    private String instructor;

    private Double rating;

    private Boolean active;

    public enum CourseType {
        ONLINE,
        WORKSHOP,
        WEBINAR,
        BOOTCAMP
    }
}