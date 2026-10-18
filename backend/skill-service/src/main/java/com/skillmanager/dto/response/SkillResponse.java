
package com.skillmanager.dto.response;

import com.skillmanager.model.Skill;
import com.skillmanager.model.SkillCategory;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

/**
 * Response payload representing a skill returned by the API.
 *
 * @param id unique identifier of the skill
 * @param name display name of the skill
 * @param normalizedName normalized name used for consistent comparisons
 * @param category category assigned to the skill
 * @param createdAt timestamp when the skill was created
 * @param updatedAt timestamp when the skill was last updated
 */
@Schema(
        name = "SkillResponse",
        description = "Skill details returned by the API."
)
public record SkillResponse(

        @Schema(description = "Unique identifier of the skill.", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Display name of the skill.", example = "Spring Boot")
        String name,

        @Schema(description = "Normalized name used for consistent comparisons.", example = "spring boot")
        String normalizedName,

        @Schema(description = "Category assigned to the skill.", example = "FRAMEWORK")
        SkillCategory category,

        @Schema(description = "Timestamp when the skill was created.", example = "2026-10-18T10:15:30Z")
        Instant createdAt,

        @Schema(description = "Timestamp when the skill was last updated.", example = "2026-10-18T10:15:30Z")
        Instant updatedAt

) {

    /**
     * Creates a response DTO from a skill entity.
     *
     * @param skill skill entity to convert
     * @return a response containing the skill details
     */
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
