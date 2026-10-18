package com.skillmanager.dto.request;

import com.skillmanager.model.SkillCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateSkillRequest(

        @NotBlank(message = "Skill name is required")
        @Size(min = 2, max = 100, message = "Skill name must contain between 2 and 100 characters")
        String name,

        @NotNull(message = "Skill category is required")
        SkillCategory category

) {
}