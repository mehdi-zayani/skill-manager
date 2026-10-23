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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link SkillService}.
 *
 * <p>These tests verify the business logic for creating, retrieving,
 * updating, deleting, searching, and suggesting skills.</p>
 *
 * <p>The repository is mocked to isolate the service from the database.
 * Mockito is used to configure repository responses and verify interactions,
 * while AssertJ is used to assert results and exceptions.</p>
 */
@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillService;

    /**
     * Verifies that a skill is normalized, saved, and returned successfully.
     */
    @Test
    void createSkillShouldSaveAndReturnSkill() {
        // Arrange
        CreateSkillRequest request = new CreateSkillRequest(
                "Spring Boot",
                SkillCategory.FRAMEWORK
        );

        when(skillRepository.existsByNormalizedName("spring boot"))
                .thenReturn(false);

        when(skillRepository.save(any(Skill.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        SkillResponse response = skillService.createSkill(request);

        // Assert
        assertThat(response.name()).isEqualTo("Spring Boot");
        assertThat(response.normalizedName()).isEqualTo("spring boot");
        assertThat(response.category()).isEqualTo(SkillCategory.FRAMEWORK);

        verify(skillRepository).existsByNormalizedName("spring boot");
        verify(skillRepository).save(any(Skill.class));
    }

    /**
     * Verifies that creating a skill with an existing normalized name
     * throws a {@link DuplicateSkillException}.
     */
    @Test
    void createSkillShouldThrowExceptionWhenSkillAlreadyExists() {
        // Arrange
        CreateSkillRequest request = new CreateSkillRequest(
                "Spring Boot",
                SkillCategory.FRAMEWORK
        );

        when(skillRepository.existsByNormalizedName("spring boot"))
                .thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> skillService.createSkill(request))
                .isInstanceOf(DuplicateSkillException.class);

        verify(skillRepository).existsByNormalizedName("spring boot");
        verify(skillRepository, never()).save(any(Skill.class));
    }

    /**
     * Verifies that an existing skill can be retrieved by its identifier.
     */
    @Test
    void getSkillByIdShouldReturnSkillWhenFound() {
        // Arrange
        UUID id = UUID.randomUUID();

        Skill skill = new Skill(
                "Spring Boot",
                "spring boot",
                SkillCategory.FRAMEWORK
        );

        when(skillRepository.findById(id))
                .thenReturn(Optional.of(skill));

        // Act
        SkillResponse response = skillService.getSkillById(id);

        // Assert
        assertThat(response.name()).isEqualTo("Spring Boot");
        assertThat(response.normalizedName()).isEqualTo("spring boot");
        assertThat(response.category()).isEqualTo(SkillCategory.FRAMEWORK);

        verify(skillRepository).findById(id);
    }

    /**
     * Verifies that retrieving a nonexistent skill throws
     * a {@link SkillNotFoundException}.
     */
    @Test
    void getSkillByIdShouldThrowExceptionWhenSkillNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();

        when(skillRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> skillService.getSkillById(id))
                .isInstanceOf(SkillNotFoundException.class);

        verify(skillRepository).findById(id);
    }

    /**
     * Verifies that an existing skill can be updated and returned.
     */
    @Test
    void updateSkillShouldUpdateAndReturnSkill() {
        // Arrange
        UUID id = UUID.randomUUID();

        Skill skill = new Skill(
                "Spring",
                "spring",
                SkillCategory.FRAMEWORK
        );

        UpdateSkillRequest request = new UpdateSkillRequest(
                "Spring Boot",
                SkillCategory.FRAMEWORK
        );

        when(skillRepository.findById(id))
                .thenReturn(Optional.of(skill));

        when(skillRepository.existsByNormalizedNameAndIdNot("spring boot", id))
                .thenReturn(false);

        when(skillRepository.save(any(Skill.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        SkillResponse response = skillService.updateSkill(id, request);

        // Assert
        assertThat(response.name()).isEqualTo("Spring Boot");
        assertThat(response.normalizedName()).isEqualTo("spring boot");
        assertThat(response.category()).isEqualTo(SkillCategory.FRAMEWORK);

        verify(skillRepository).findById(id);
        verify(skillRepository).existsByNormalizedNameAndIdNot("spring boot", id);
        verify(skillRepository).save(skill);
    }

    /**
     * Verifies that updating a nonexistent skill throws
     * a {@link SkillNotFoundException} without checking uniqueness or saving.
     */
    @Test
    void updateSkillShouldThrowExceptionWhenSkillNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();

        UpdateSkillRequest request = new UpdateSkillRequest(
                "Spring Boot",
                SkillCategory.FRAMEWORK
        );

        when(skillRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> skillService.updateSkill(id, request))
                .isInstanceOf(SkillNotFoundException.class);

        verify(skillRepository).findById(id);
        verify(skillRepository, never())
                .existsByNormalizedNameAndIdNot(any(String.class), any(UUID.class));
        verify(skillRepository, never()).save(any(Skill.class));
    }

    /**
     * Verifies that updating a skill to a name already used by another skill
     * throws a {@link DuplicateSkillException}.
     */
    @Test
    void updateSkillShouldThrowExceptionWhenNameAlreadyExists() {
        // Arrange
        UUID id = UUID.randomUUID();

        Skill skill = new Skill(
                "Spring",
                "spring",
                SkillCategory.FRAMEWORK
        );

        UpdateSkillRequest request = new UpdateSkillRequest(
                "Spring Boot",
                SkillCategory.FRAMEWORK
        );

        when(skillRepository.findById(id))
                .thenReturn(Optional.of(skill));

        when(skillRepository.existsByNormalizedNameAndIdNot("spring boot", id))
                .thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> skillService.updateSkill(id, request))
                .isInstanceOf(DuplicateSkillException.class);

        verify(skillRepository).findById(id);
        verify(skillRepository).existsByNormalizedNameAndIdNot("spring boot", id);
        verify(skillRepository, never()).save(any(Skill.class));
    }

    /**
     * Verifies that an existing skill is deleted successfully.
     */
    @Test
    void deleteSkillShouldDeleteSkillWhenFound() {
        // Arrange
        UUID id = UUID.randomUUID();

        Skill skill = new Skill(
                "Spring Boot",
                "spring boot",
                SkillCategory.FRAMEWORK
        );

        when(skillRepository.findById(id))
                .thenReturn(Optional.of(skill));

        // Act
        skillService.deleteSkill(id);

        // Assert
        verify(skillRepository).findById(id);
        verify(skillRepository).delete(skill);
    }

    /**
     * Verifies that deleting a nonexistent skill throws
     * a {@link SkillNotFoundException}.
     */
    @Test
    void deleteSkillShouldThrowExceptionWhenSkillNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();

        when(skillRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> skillService.deleteSkill(id))
                .isInstanceOf(SkillNotFoundException.class);

        verify(skillRepository).findById(id);
        verify(skillRepository, never()).delete(any(Skill.class));
    }

    /**
     * Verifies that all skills are returned when no search or category filter
     * is provided, while preserving pagination metadata.
     */
    @Test
    void getSkillsShouldReturnAllSkillsWhenNoFilterIsProvided() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        List<Skill> skills = List.of(
                new Skill("Java", "java", SkillCategory.LANGUAGE),
                new Skill("Spring Boot", "spring boot", SkillCategory.FRAMEWORK)
        );

        Page<Skill> skillPage = new PageImpl<>(skills, pageable, skills.size());

        when(skillRepository.findAll(pageable)).thenReturn(skillPage);

        // Act
        PageResponse<SkillResponse> response =
                skillService.getSkills(null, null, pageable);

        // Assert
        assertThat(response.items()).hasSize(2);
        assertThat(response.items())
                .extracting(SkillResponse::name)
                .containsExactly("Java", "Spring Boot");

        verify(skillRepository).findAll(pageable);
    }

    /**
     * Verifies that skills can be searched by name and that the search term
     * is trimmed and normalized before reaching the repository.
     */
    @Test
    void getSkillsShouldSearchByNameWhenSearchIsProvided() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        String search = "  Spring   Boot ";

        List<Skill> skills = List.of(
                new Skill("Spring Boot", "spring boot", SkillCategory.FRAMEWORK)
        );

        Page<Skill> skillPage = new PageImpl<>(skills, pageable, skills.size());

        when(skillRepository.searchByName("spring boot", pageable))
                .thenReturn(skillPage);

        // Act
        PageResponse<SkillResponse> response =
                skillService.getSkills(search, null, pageable);

        // Assert
        assertThat(response.items())
                .extracting(SkillResponse::name)
                .containsExactly("Spring Boot");

        verify(skillRepository).searchByName("spring boot", pageable);
    }

    /**
     * Verifies that skills can be filtered by category.
     */
    @Test
    void getSkillsShouldFilterByCategoryWhenCategoryIsProvided() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        List<Skill> skills = List.of(
                new Skill("Spring Boot", "spring boot", SkillCategory.FRAMEWORK),
                new Skill("Angular", "angular", SkillCategory.FRAMEWORK)
        );

        Page<Skill> skillPage = new PageImpl<>(skills, pageable, skills.size());

        when(skillRepository.findByCategory(SkillCategory.FRAMEWORK, pageable))
                .thenReturn(skillPage);

        // Act
        PageResponse<SkillResponse> response =
                skillService.getSkills(null, SkillCategory.FRAMEWORK, pageable);

        // Assert
        assertThat(response.items()).hasSize(2);
        assertThat(response.items())
                .extracting(SkillResponse::name)
                .containsExactly("Spring Boot", "Angular");

        verify(skillRepository).findByCategory(SkillCategory.FRAMEWORK, pageable);
    }

    /**
     * Verifies that the search term and category filter are applied together.
     */
    @Test
    void getSkillsShouldSearchByNameAndCategoryWhenBothAreProvided() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        List<Skill> skills = List.of(
                new Skill("Spring Boot", "spring boot", SkillCategory.FRAMEWORK)
        );

        Page<Skill> skillPage = new PageImpl<>(skills, pageable, skills.size());

        when(skillRepository.searchByNameAndCategory(
                "spring", SkillCategory.FRAMEWORK, pageable))
                .thenReturn(skillPage);

        // Act
        PageResponse<SkillResponse> response =
                skillService.getSkills("Spring", SkillCategory.FRAMEWORK, pageable);

        // Assert
        assertThat(response.items())
                .extracting(SkillResponse::name)
                .containsExactly("Spring Boot");

        verify(skillRepository).searchByNameAndCategory(
                "spring", SkillCategory.FRAMEWORK, pageable);
    }

    /**
     * Verifies that suggestions are returned in repository-defined order
     * for a valid query.
     */
    @Test
    void getSuggestionsShouldReturnMatchingSuggestions() {
        // Arrange
        List<Skill> skills = List.of(
                new Skill("Spring", "spring", SkillCategory.FRAMEWORK),
                new Skill("Spring Boot", "spring boot", SkillCategory.FRAMEWORK)
        );

        when(skillRepository
                .findTop10ByNormalizedNameStartingWithOrderByNormalizedNameAsc("spring"))
                .thenReturn(skills);

        // Act
        List<SkillSuggestionResponse> response =
                skillService.getSuggestions("  Spring  ");

        // Assert
        assertThat(response)
                .extracting(SkillSuggestionResponse::name)
                .containsExactly("Spring", "Spring Boot");

        verify(skillRepository)
                .findTop10ByNormalizedNameStartingWithOrderByNormalizedNameAsc("spring");
    }

    /**
     * Verifies that a null suggestion query returns an empty list
     * without accessing the repository.
     */
    @Test
    void getSuggestionsShouldReturnEmptyListWhenQueryIsNull() {
        // Act
        List<SkillSuggestionResponse> response =
                skillService.getSuggestions(null);

        // Assert
        assertThat(response).isEmpty();

        verifyNoInteractions(skillRepository);
    }

    /**
     * Verifies that a suggestion query shorter than two characters
     * returns an empty list without accessing the repository.
     */
    @Test
    void getSuggestionsShouldReturnEmptyListWhenQueryIsTooShort() {
        // Act
        List<SkillSuggestionResponse> response =
                skillService.getSuggestions(" J ");

        // Assert
        assertThat(response).isEmpty();

        verifyNoInteractions(skillRepository);
    }
}
