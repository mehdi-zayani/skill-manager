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

public interface SkillRepository extends JpaRepository<Skill, UUID> {

    boolean existsByNormalizedName(String normalizedName);

    boolean existsByNormalizedNameAndIdNot(
            String normalizedName,
            UUID id
    );

    Page<Skill> findByCategory(
            SkillCategory category,
            Pageable pageable
    );

    @Query("""
            SELECT s FROM Skill s
            WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))
               OR s.normalizedName LIKE CONCAT('%', LOWER(:search), '%')
            """)
    Page<Skill> searchByName(
            @Param("search") String search,
            Pageable pageable
    );

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

    List<Skill> findTop10ByNormalizedNameStartingWithOrderByNormalizedNameAsc(
            String normalizedName
    );
}