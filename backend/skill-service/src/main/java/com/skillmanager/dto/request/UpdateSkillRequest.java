package com.skillmanager.dto.request;

import com.skillmanager.model.SkillCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request payload used to update an existing skill.
 *
 * @param name updated display name of the skill; must contain between
 *             2 and 100 characters
 * @param category updated category assigned to the skill; required
 */
@Schema(
        name = "UpdateSkillRequest",
        description = "Data required to update an existing skill."
)
public record UpdateSkillRequest(

        @Schema(
                description = "Updated display name of the skill.",
                example = "Spring Boot",
                minLength = 2,
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Skill name is required")
        @Size(min = 2, max = 100, message = "Skill name must contain between 2 and 100 characters")
        String name,

        @Schema(
                description = "Updated category assigned to the skill.",
                example = "FRAMEWORK",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Skill category is required")
        SkillCategory category

) {
}
