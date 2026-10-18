package com.skillmanager.dto.response;

import com.skillmanager.model.Skill;
import com.skillmanager.model.SkillCategory;

import java.util.UUID;

public record SkillSuggestionResponse(
        UUID id,
        String name,
        SkillCategory category
) {

    public static SkillSuggestionResponse from(Skill skill) {
        return new SkillSuggestionResponse(
                skill.getId(),
                skill.getName(),
                skill.getCategory()
        );
    }
}