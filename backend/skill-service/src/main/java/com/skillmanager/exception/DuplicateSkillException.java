package com.skillmanager.exception;

public class DuplicateSkillException extends RuntimeException {

    public DuplicateSkillException(String name) {
        super("A skill with the name '" + name + "' already exists");
    }
}