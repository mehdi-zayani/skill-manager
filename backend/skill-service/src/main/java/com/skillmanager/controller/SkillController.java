
package com.skillmanager.controller;

import com.skillmanager.dto.request.CreateSkillRequest;
import com.skillmanager.dto.request.UpdateSkillRequest;
import com.skillmanager.dto.response.PageResponse;
import com.skillmanager.dto.response.SkillResponse;
import com.skillmanager.dto.response.SkillSuggestionResponse;
import com.skillmanager.model.SkillCategory;
import com.skillmanager.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Exposes REST endpoints for creating, retrieving, updating,
 * deleting, searching, and suggesting IT skills.
 *
 * <p>All endpoints are available under {@code /api/v1/skills}.
 * Business logic is delegated to {@link SkillService}.</p>
 */
@RestController
@RequestMapping("/api/v1/skills")
@Tag(
        name = "Skills",
        description = "Manage IT skills, search the catalog, and retrieve autocomplete suggestions."
)
public class SkillController {

    private final SkillService skillService;

    /**
     * Creates the controller with its required service dependency.
     *
     * @param skillService service responsible for skill business operations
     */
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    /**
     * Creates a new skill.
     *
     * @param request validated data for the skill to create
     * @return the created skill
     */
    @Operation(
            summary = "Create a skill",
            description = "Creates a skill with a name and category. Skill names must be unique after normalization."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Skill created successfully",
                    content = @Content(
                            schema = @Schema(implementation = SkillResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or validation failure"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "A skill with the same normalized name already exists"
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SkillResponse createSkill(
            @Valid @RequestBody CreateSkillRequest request) {
        return skillService.createSkill(request);
    }

    /**
     * Retrieves skills with optional search, category filtering,
     * pagination, and sorting.
     *
     * @param search optional text used to search skill names
     * @param category optional category filter
     * @param page zero-based page index; negative values are treated as zero
     * @param size requested page size, limited to the range from 1 to 100
     * @param sortBy field used for sorting; unsupported values default to name
     * @param direction sort direction; desc selects descending order,
     *                  otherwise ascending order is used
     * @return a paginated response containing the matching skills
     */
    @Operation(
            summary = "List and search skills",
            description = "Returns a paginated skill list with optional name search, category filtering, and sorting."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Skills retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = PageResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid category or query parameter"
            )
    })
    @GetMapping
    public PageResponse<SkillResponse> getSkills(
            @Parameter(description = "Optional case-insensitive search term for skill names.")
            @RequestParam(required = false) String search,

            @Parameter(description = "Optional category used to filter skills.")
            @RequestParam(required = false) SkillCategory category,

            @Parameter(description = "Zero-based page index. Negative values default to 0.", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size, limited to 1–100.", example = "20")
            @RequestParam(defaultValue = "20") int size,

            @Parameter(
                    description = "Field used to sort results. Supported values: name, category, createdAt, updatedAt.",
                    schema = @Schema(allowableValues = {"name", "category", "createdAt", "updatedAt"})
            )
            @RequestParam(defaultValue = "name") String sortBy,

            @Parameter(
                    description = "Sort direction. Use asc or desc.",
                    schema = @Schema(allowableValues = {"asc", "desc"})
            )
            @RequestParam(defaultValue = "asc") String direction) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 100);

        String safeSortBy = switch (sortBy) {
            case "name", "category", "createdAt", "updatedAt" -> sortBy;
            default -> "name";
        };

        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                safePage,
                safeSize,
                Sort.by(sortDirection, safeSortBy)
        );

        return skillService.getSkills(search, category, pageable);
    }

    /**
     * Retrieves autocomplete suggestions for a skill name prefix.
     *
     * @param q prefix entered by the user
     * @return matching skill suggestions
     */
    @Operation(
            summary = "Get skill suggestions",
            description = "Returns up to ten existing skills whose normalized names start with the supplied query. Queries shorter than two characters return an empty list."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Suggestions retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = SkillSuggestionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "The required query parameter is missing"
            )
    })
    @GetMapping("/suggestions")
    public List<SkillSuggestionResponse> getSuggestions(
            @Parameter(
                    description = "Skill name prefix used to retrieve autocomplete suggestions.",
                    example = "spr",
                    required = true
            )
            @RequestParam String q) {
        return skillService.getSuggestions(q);
    }

    /**
     * Retrieves a skill by its unique identifier.
     *
     * @param id UUID of the requested skill
     * @return the requested skill
     */
    @Operation(
            summary = "Get a skill by ID",
            description = "Retrieves the details of a single skill using its UUID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Skill retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = SkillResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No skill exists with the specified ID"
            )
    })
    @GetMapping("/{id}")
    public SkillResponse getSkillById(
            @Parameter(
                    description = "UUID of the skill to retrieve.",
                    required = true
            )
            @PathVariable UUID id) {
        return skillService.getSkillById(id);
    }

    /**
     * Updates an existing skill.
     *
     * @param id UUID of the skill to update
     * @param request validated data containing the updated name and category
     * @return the updated skill
     */
    @Operation(
            summary = "Update a skill",
            description = "Updates an existing skill's name and category. The normalized name must remain unique."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Skill updated successfully",
                    content = @Content(
                            schema = @Schema(implementation = SkillResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body or validation failure"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No skill exists with the specified ID"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Another skill already uses the requested normalized name"
            )
    })
    @PutMapping("/{id}")
    public SkillResponse updateSkill(
            @Parameter(
                    description = "UUID of the skill to update.",
                    required = true
            )
            @PathVariable UUID id,

            @Valid @RequestBody UpdateSkillRequest request) {
        return skillService.updateSkill(id, request);
    }

    /**
     * Deletes an existing skill.
     *
     * @param id UUID of the skill to delete
     */
    @Operation(
            summary = "Delete a skill",
            description = "Permanently deletes a skill from the catalog."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Skill deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No skill exists with the specified ID"
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSkill(
            @Parameter(
                    description = "UUID of the skill to delete.",
                    required = true
            )
            @PathVariable UUID id) {
        skillService.deleteSkill(id);
    }
}
