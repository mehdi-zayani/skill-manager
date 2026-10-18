package com.skillmanager.exception;

import java.util.UUID;

/**
 * Exception thrown when a requested skill cannot be found.
 */
public class SkillNotFoundException extends RuntimeException {

    /**
     * Creates an exception for a skill that does not exist.
     *
     * @param id unique identifier of the skill that could not be found
     */
    public SkillNotFoundException(UUID id) {
        super("Skill not found with id: " + id);
    }
}
