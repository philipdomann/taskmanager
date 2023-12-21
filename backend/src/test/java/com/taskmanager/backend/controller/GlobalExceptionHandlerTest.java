package com.taskmanager.backend.controller;

import com.taskmanager.backend.model.Task;
import com.taskmanager.backend.service.TaskUpdateConflictException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        webRequest = new ServletWebRequest(new MockHttpServletRequest());
    }

    @Test
    void handleMethodArgumentNotValid() {
        Task invalidTask = Task.builder()
                .name("")
                .priority(null)
                .build();

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(invalidTask, "task");
        bindingResult.addError(new FieldError("task", "name", "Name must not be empty."));
        bindingResult.addError(new FieldError("task", "priority", "Priority must not be null."));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Object> response = exceptionHandler.handleMethodArgumentNotValid(ex);
        assertErrorResponse(response, HttpStatus.BAD_REQUEST, "Validation failed");
    }

    @Test
    void handleNoSuchElement() {
        NoSuchElementException ex = new NoSuchElementException("No such element");
        ResponseEntity<Object> response = exceptionHandler.handleNoSuchElement(ex);
        assertErrorResponse(response, HttpStatus.NOT_FOUND, "No such element");
    }

    @Test
    void handleRuntimeException() {
        RuntimeException ex = new RuntimeException("Runtime exception");
        ResponseEntity<Object> response = exceptionHandler.handleRuntimeException(ex);
        assertErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    @Test
    void handleTaskUpdateConflict() {
        TaskUpdateConflictException ex = new TaskUpdateConflictException(null);
        ResponseEntity<Object> response = exceptionHandler.handleTaskUpdateConflict(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());}

    @Test
    void handleEntityNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");
        ResponseEntity<String> response = exceptionHandler.handleEntityNotFound(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Entity not found", response.getBody());
    }

    private void assertErrorResponse(ResponseEntity<Object> response, HttpStatus expectedStatus, String expectedMessage) {
        assertEquals(expectedStatus, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedMessage, ((Map<String, Object>) response.getBody()).get("message"));}
}

