package com.skillmanager.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Generic response wrapper for paginated results.
 *
 * @param <T> type of the items contained in the page
 * @param items content of the current page
 * @param page zero-based index of the current page
 * @param size maximum number of items requested per page
 * @param totalElements total number of elements matching the query
 * @param totalPages total number of pages available
 */
@Schema(
        name = "PageResponse",
        description = "Paginated response containing items and pagination metadata."
)
public record PageResponse<T>(

        @Schema(description = "Items returned for the current page.")
        List<T> items,

        @Schema(description = "Zero-based index of the current page.", example = "0")
        int page,

        @Schema(description = "Maximum number of items per page.", example = "10")
        int size,

        @Schema(description = "Total number of elements matching the query.", example = "42")
        long totalElements,

        @Schema(description = "Total number of pages available.", example = "5")
        int totalPages

) {

    /**
     * Creates a pagination response from a Spring Data page.
     *
     * @param result Spring Data page containing the query results and pagination metadata
     * @param <T> type of the items contained in the page
     * @return a response containing the page content and pagination metadata
     */
    public static <T> PageResponse<T> from(Page<T> result) {
        return new PageResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }
}
