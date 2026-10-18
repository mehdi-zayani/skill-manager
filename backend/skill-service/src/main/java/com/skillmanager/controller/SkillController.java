package com.skillmanager.controller;

import com.skillmanager.dto.request.CreateSkillRequest;
import com.skillmanager.dto.request.UpdateSkillRequest;
import com.skillmanager.dto.response.PageResponse;
import com.skillmanager.dto.response.SkillResponse;
import com.skillmanager.dto.response.SkillSuggestionResponse;
import com.skillmanager.model.SkillCategory;
import com.skillmanager.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SkillResponse createSkill(
            @Valid @RequestBody CreateSkillRequest request) {
        return skillService.createSkill(request);
    }

    @GetMapping
    public PageResponse<SkillResponse> getSkills(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) SkillCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
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

    @GetMapping("/suggestions")
    public List<SkillSuggestionResponse> getSuggestions(
            @RequestParam String q) {
        return skillService.getSuggestions(q);
    }

    @GetMapping("/{id}")
    public SkillResponse getSkillById(@PathVariable UUID id) {
        return skillService.getSkillById(id);
    }

    @PutMapping("/{id}")
    public SkillResponse updateSkill(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSkillRequest request) {
        return skillService.updateSkill(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSkill(@PathVariable UUID id) {
        skillService.deleteSkill(id);
    }
}