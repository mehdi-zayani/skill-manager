
package com.skillmanager.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents an IT skill stored in the database.
 *
 * <p>Each skill has a unique normalized name, a category, and creation
 * and update timestamps.</p>
 */
@Entity
@Table(
        name = "skills",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_skills_normalized_name",
                        columnNames = "normalized_name"
                )
        }
)
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Stores a normalized version of the skill name to support
     * consistent searches and case-insensitive duplicate detection.
     */
    @Column(name = "normalized_name", nullable = false, length = 100)
    private String normalizedName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SkillCategory category;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Required by JPA for entity instantiation.
     */
    public Skill() {
    }

    /**
     * Creates a skill with its name, normalized name, and category.
     *
     * @param name the display name of the skill
     * @param normalizedName the normalized name used for consistent matching
     * @param category the category assigned to the skill
     */
    public Skill(String name, String normalizedName, SkillCategory category) {
        this.name = name;
        this.normalizedName = normalizedName;
        this.category = category;
    }

    /**
     * Initializes the timestamps before the entity is first persisted.
     */
    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    /**
     * Updates the modification timestamp before an entity update is persisted.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public SkillCategory getCategory() {
        return category;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public void setCategory(SkillCategory category) {
        this.category = category;
    }
}
