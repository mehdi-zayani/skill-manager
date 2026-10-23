
package com.skillmanager.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Handles application exceptions and converts them into consistent HTTP error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles requests for skills that do not exist.
     *
     * @param exception exception containing details about the missing skill
     * @return a 404 Not Found response
     */
    @ExceptionHandler(SkillNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleSkillNotFound(
            SkillNotFoundException exception) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );
    }

    /**
     * Handles attempts to create or update a skill that conflicts with an existing skill.
     *
     * @param exception exception describing the duplicate skill
     * @return a 409 Conflict response
     */
    @ExceptionHandler(DuplicateSkillException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateSkill(
            DuplicateSkillException exception) {

        return buildResponse(
                HttpStatus.CONFLICT,
                exception.getMessage()
        );
    }

    /**
     * Handles validation failures on request payloads.
     *
     * @param exception exception containing the request validation errors
     * @return a 400 Bad Request response with details for each invalid field
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null
                                ? error.getDefaultMessage()
                                : "Invalid value",
                        (first, second) -> first
                ));

        Map<String, Object> body = Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Validation failed",
                "details", errors
        );

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Handles unreadable request bodies, including invalid JSON or category values.
     *
     * @param exception exception raised while reading the request body
     * @return a 400 Bad Request response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidRequestBody(
            HttpMessageNotReadableException exception) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid request body or category value"
        );
    }

    /**
     * Handles database integrity violations that prevent a request from being completed.
     *
     * @param exception exception raised by the data access layer
     * @return a 409 Conflict response
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException exception) {

        return buildResponse(
                HttpStatus.CONFLICT,
                "The request conflicts with existing data"
        );
    }

    /**
     * Builds a standard error response containing a timestamp, HTTP status, error description,
     * and message.
     *
     * @param status HTTP status to return
     * @param message error message to include in the response
     * @return a response entity containing the error details
     */
    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String message) {

        Map<String, Object> body = Map.of(
                "timestamp", Instant.now(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        );

        return ResponseEntity.status(status).body(body);
    }
}
