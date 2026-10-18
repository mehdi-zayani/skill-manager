package com.skillmanager.dto.response;

import com.skillmanager.model.Skill;
import com.skillmanager.model.SkillCategory;

import java.time.Instant;
import java.util.UUID;

public record SkillResponse(
        UUID id,
        String name,
        String normalizedName,
        SkillCategory category,
        Instant createdAt,
        Instant updatedAt
) {

    public static SkillResponse from(Skill skill) {
        return new SkillResponse(
                skill.getId(),
                skill.getName(),
                skill.getNormalizedName(),
                skill.getCategory(),
                skill.getCreatedAt(),
                skill.getUpdatedAt()
        );
    }
}