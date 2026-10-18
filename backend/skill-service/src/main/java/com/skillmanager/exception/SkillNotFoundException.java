package com.skillmanager.exception;

import java.util.UUID;

public class SkillNotFoundException extends RuntimeException {

    public SkillNotFoundException(UUID id) {
        super("Skill not found with id: " + id);
    }
}