package com.skillmanager.service;

import com.skillmanager.dto.request.CreateSkillRequest;
import com.skillmanager.dto.request.UpdateSkillRequest;
import com.skillmanager.dto.response.SkillResponse;
import com.skillmanager.dto.response.SkillSuggestionResponse;
import com.skillmanager.exception.DuplicateSkillException;
import com.skillmanager.exception.SkillNotFoundException;
import com.skillmanager.model.Skill;
import com.skillmanager.model.SkillCategory;
import com.skillmanager.repository.SkillRepository;
import org.springframework.data.domain.Page;
import com.skillmanager.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

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
    public SkillResponse getSkillById(UUID id) {
        return SkillResponse.from(findSkill(id));
    }

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

    @Transactional
    public void deleteSkill(UUID id) {
        Skill skill = findSkill(id);
        skillRepository.delete(skill);
    }

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

    private Skill findSkill(UUID id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new SkillNotFoundException(id));
    }

    private String normalizeName(String name) {
        return name.trim()
                .replaceAll("\\s+", " ")
                .toLowerCase(Locale.ROOT);
    }
}