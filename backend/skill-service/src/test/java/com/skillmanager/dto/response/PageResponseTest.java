package com.skillmanager.dto.response;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link PageResponse}.
 */
class PageResponseTest {

    @Test
    void fromShouldMapPageContentAndMetadata() {
        // Arrange
        List<String> items = List.of("Java", "Spring Boot", "PostgreSQL");

        Page<String> page = new PageImpl<>(
                items,
                PageRequest.of(0, 10),
                23
        );

        // Act
        PageResponse<String> response = PageResponse.from(page);

        // Assert
        assertThat(response.items()).containsExactly("Java", "Spring Boot", "PostgreSQL");
        assertThat(response.page()).isZero();
        assertThat(response.size()).isEqualTo(10);
        assertThat(response.totalElements()).isEqualTo(23);
        assertThat(response.totalPages()).isEqualTo(3);
    }
}
