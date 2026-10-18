
package com.skillmanager.dto.response;

import com.skillmanager.model.Skill;
import com.skillmanager.model.SkillCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Response payload representing a skill suggestion.
 *
 * @param id unique identifier of the suggested skill
 * @param name display name of the suggested skill
 * @param category category assigned to the suggested skill
 */
@Schema(
        name = "SkillSuggestionResponse",
        description = "Skill information returned when suggesting matching skills."
)
public record SkillSuggestionResponse(

        @Schema(
                description = "Unique identifier of the suggested skill.",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        UUID id,

        @Schema(
                description = "Display name of the suggested skill.",
                example = "Spring Boot"
        )
        String name,

        @Schema(
                description = "Category assigned to the suggested skill.",
                example = "FRAMEWORK"
        )
        SkillCategory category

) {

    /**
     * Creates a suggestion response from a skill entity.
     *
     * @param skill skill entity to convert
     * @return a response containing the suggested skill details
     */
    public static SkillSuggestionResponse from(Skill skill) {
        return new SkillSuggestionResponse(
                skill.getId(),
                skill.getName(),
                skill.getCategory()
        );
    }
}
