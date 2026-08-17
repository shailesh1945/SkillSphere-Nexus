package com.skillsphere.nexus.skill_service.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetencyGapResponse {

    private String skillName;

    private Integer currentProficiency;

    private Integer requiredProficiency;

    private Integer gap;

}