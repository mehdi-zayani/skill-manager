
package com.skillmanager.service;

import com.skillmanager.dto.request.CreateSkillRequest;
import com.skillmanager.dto.request.UpdateSkillRequest;
import com.skillmanager.dto.response.PageResponse;
import com.skillmanager.dto.response.SkillResponse;
import com.skillmanager.dto.response.SkillSuggestionResponse;
import com.skillmanager.exception.DuplicateSkillException;
import com.skillmanager.exception.SkillNotFoundException;
import com.skillmanager.model.Skill;
import com.skillmanager.model.SkillCategory;
import com.skillmanager.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Implements the business logic for managing IT skills.
 *
 * <p>Coordinates skill creation, retrieval, updates, deletion, searching,
 * and autocomplete suggestions. Delegates data access to
 * {@link SkillRepository} and converts entities into response DTOs.</p>
 *
 * <p>Database operations are read-only by default. Methods that modify
 * data explicitly enable write transactions.</p>
 */
@Service
@Transactional(readOnly = true)
public class SkillService {

    private final SkillRepository skillRepository;

    /**
     * Creates the service with its required repository dependency.
     *
     * @param skillRepository repository used to access skill data
     */
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    /**
     * Creates a skill after normalizing its name and checking for duplicates.
     *
     * @param request validated data required to create the skill
     * @return the response representation of the created skill
     * @throws DuplicateSkillException if a skill with the same normalized
     *         name already exists
     */
    @Transactional
    public SkillResponse createSkill(CreateSkillRequest request) {
        String name = request.name().trim();
        String normalizedName = normalizeName(name);

        if (skillRepository.existsByNormalizedName(normalizedName)) {
            throw new DuplicateSkillException(name);
        }

        Skill skill = new Skill(name, normalizedName, request.category());

        return SkillResponse.from(skillRepository.save(skill));
    }

    /**
     * Retrieves skills using optional name search, category filtering,
     * pagination, and sorting criteria.
     *
     * @param search optional text used to search skill names
     * @param category optional category used to filter results
     * @param pageable pagination and sorting information
     * @return a paginated response containing the matching skills
     */
    public PageResponse<SkillResponse> getSkills(
            String search,
            SkillCategory category,
            Pageable pageable) {

        boolean hasSearch = search != null && !search.isBlank();

        Page<Skill> skills;

        if (hasSearch && category != null) {
            skills = skillRepository.searchByNameAndCategory(
                    normalizeName(search.trim()),
                    category,
                    pageable
            );
        } else if (hasSearch) {
            skills = skillRepository.searchByName(
                    normalizeName(search.trim()),
                    pageable
            );
        } else if (category != null) {
            skills = skillRepository.findByCategory(category, pageable);
        } else {
            skills = skillRepository.findAll(pageable);
        }

        Page<SkillResponse> responsePage = skills.map(SkillResponse::from);

        return PageResponse.from(responsePage);
    }

    /**
     * Retrieves a skill by its unique identifier.
     *
     * @param id the UUID of the requested skill
     * @return the response representation of the skill
     * @throws SkillNotFoundException if no skill exists with the given ID
     */
    public SkillResponse getSkillById(UUID id) {
        return SkillResponse.from(findSkill(id));
    }

    /**
     * Updates an existing skill after checking that its normalized name
     * does not conflict with another skill.
     *
     * @param id the UUID of the skill to update
     * @param request validated data containing the updated name and category
     * @return the response representation of the updated skill
     * @throws SkillNotFoundException if no skill exists with the given ID
     * @throws DuplicateSkillException if another skill already uses the
     *         requested normalized name
     */
    @Transactional
    public SkillResponse updateSkill(UUID id, UpdateSkillRequest request) {
        Skill skill = findSkill(id);

        String name = request.name().trim();
        String normalizedName = normalizeName(name);

        if (skillRepository.existsByNormalizedNameAndIdNot(
                normalizedName, id)) {
            throw new DuplicateSkillException(name);
        }

        skill.setName(name);
        skill.setNormalizedName(normalizedName);
        skill.setCategory(request.category());

        return SkillResponse.from(skillRepository.save(skill));
    }

    /**
     * Deletes an existing skill.
     *
     * @param id the UUID of the skill to delete
     * @throws SkillNotFoundException if no skill exists with the given ID
     */
    @Transactional
    public void deleteSkill(UUID id) {
        Skill skill = findSkill(id);
        skillRepository.delete(skill);
    }

    /**
     * Retrieves autocomplete suggestions for a skill name prefix.
     *
     * <p>Queries shorter than two characters return an empty list.
     * Results are limited to ten skills and ordered by normalized name.</p>
     *
     * @param query the prefix entered by the user
     * @return matching skill suggestions, or an empty list if the query
     *         is null or shorter than two characters after trimming
     */
    public List<SkillSuggestionResponse> getSuggestions(String query) {
        if (query == null || query.trim().length() < 2) {
            return List.of();
        }

        String normalizedQuery = normalizeName(query);

        return skillRepository
                .findTop10ByNormalizedNameStartingWithOrderByNormalizedNameAsc(
                        normalizedQuery
                )
                .stream()
                .map(SkillSuggestionResponse::from)
                .toList();
    }

    /**
     * Finds a skill by ID or raises the domain-specific not-found exception.
     *
     * @param id the UUID of the skill to find
     * @return the matching skill entity
     * @throws SkillNotFoundException if the skill does not exist
     */
    private Skill findSkill(UUID id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new SkillNotFoundException(id));
    }

    /**
     * Normalizes a skill name for consistent matching and duplicate detection.
     *
     * <p>Leading and trailing whitespace is removed, consecutive whitespace
     * characters are collapsed into a single space, and the result is
     * converted to lowercase using a locale-independent rule.</p>
     *
     * @param name the skill name to normalize
     * @return the normalized skill name
     */
    private String normalizeName(String name) {
        return name.trim()
                .replaceAll("\\s+", " ")
                .toLowerCase(Locale.ROOT);
    }
}
