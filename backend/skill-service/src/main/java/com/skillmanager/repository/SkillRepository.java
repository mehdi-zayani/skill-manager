
package com.skillmanager.repository;

import com.skillmanager.model.Skill;
import com.skillmanager.model.SkillCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Provides database access operations for {@link Skill} entities.
 *
 * <p>Extends {@link JpaRepository} to inherit standard CRUD operations
 * and defines additional queries for duplicate detection, filtering,
 * searching, and skill suggestions.</p>
 */
public interface SkillRepository extends JpaRepository<Skill, UUID> {

    /**
     * Checks whether a skill already exists with the specified normalized name.
     *
     * @param normalizedName the normalized skill name to check
     * @return {@code true} if a skill with that normalized name exists;
     *         {@code false} otherwise
     */
    boolean existsByNormalizedName(String normalizedName);

    /**
     * Checks whether another skill uses the specified normalized name,
     * excluding the skill identified by the given ID.
     *
     * <p>This is useful when updating a skill without treating its own
     * existing name as a duplicate.</p>
     *
     * @param normalizedName the normalized skill name to check
     * @param id the ID of the skill to exclude from the check
     * @return {@code true} if another matching skill exists;
     *         {@code false} otherwise
     */
    boolean existsByNormalizedNameAndIdNot(
            String normalizedName,
            UUID id
    );

    /**
     * Retrieves skills belonging to a specific category.
     *
     * @param category the category used to filter skills
     * @param pageable pagination and sorting information
     * @return a page containing the matching skills
     */
    Page<Skill> findByCategory(
            SkillCategory category,
            Pageable pageable
    );

    /**
     * Searches skills by name using a case-insensitive partial match.
     *
     * <p>The search term is matched against both the display name and
     * the normalized name.</p>
     *
     * @param search the term to search for
     * @param pageable pagination and sorting information
     * @return a page containing skills matching the search term
     */
    @Query("""
            SELECT s FROM Skill s
            WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))
               OR s.normalizedName LIKE CONCAT('%', LOWER(:search), '%')
            """)
    Page<Skill> searchByName(
            @Param("search") String search,
            Pageable pageable
    );

    /**
     * Searches skills by category and name using a case-insensitive
     * partial match.
     *
     * @param search the term to search for
     * @param category the category used to filter skills
     * @param pageable pagination and sorting information
     * @return a page containing skills matching both criteria
     */
    @Query("""
            SELECT s FROM Skill s
            WHERE s.category = :category
              AND (
                    LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))
                    OR s.normalizedName LIKE CONCAT('%', LOWER(:search), '%')
              )
            """)
    Page<Skill> searchByNameAndCategory(
            @Param("search") String search,
            @Param("category") SkillCategory category,
            Pageable pageable
    );

    /**
     * Retrieves up to ten skills whose normalized names start with
     * the specified prefix, ordered alphabetically by normalized name.
     *
     * <p>This query supports autocomplete suggestions as users enter
     * a skill name.</p>
     *
     * @param normalizedName the normalized prefix used to find suggestions
     * @return a list of matching skills, limited to ten results
     */
    List<Skill> findTop10ByNormalizedNameStartingWithOrderByNormalizedNameAsc(
            String normalizedName
    );
}
