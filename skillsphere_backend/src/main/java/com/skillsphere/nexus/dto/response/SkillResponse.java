package com.skillsphere.nexus.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class SkillResponse {

    private UUID skillId;

    private String skillName;

    private String description;

    private String category;
}
