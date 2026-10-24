
package com.skillmanager.dto.response;

import com.skillmanager.model.Skill;
import com.skillmanager.model.SkillCategory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link SkillResponse}.
 */
class SkillResponseTest {

    @Test
    void fromShouldMapSkillFields() {
        // Arrange
        Skill skill = new Skill(
                "Spring Boot",
                "spring boot",
                SkillCategory.FRAMEWORK
        );

        // Act
        SkillResponse response = SkillResponse.from(skill);

        // Assert
        assertThat(response.name()).isEqualTo("Spring Boot");
        assertThat(response.normalizedName()).isEqualTo("spring boot");
        assertThat(response.category()).isEqualTo(SkillCategory.FRAMEWORK);
        assertThat(response.id()).isNull();
        assertThat(response.createdAt()).isNull();
        assertThat(response.updatedAt()).isNull();
    }
}
