package com.skillmanager.exception;

/**
 * Exception thrown when a skill with the same name already exists.
 */
public class DuplicateSkillException extends RuntimeException {

    /**
     * Creates an exception for a duplicate skill name.
     *
     * @param name name of the skill that already exists
     */
    public DuplicateSkillException(String name) {
        super("A skill with the name '" + name + "' already exists");
    }
}
