package com.taskmanager.backend.service;

import com.taskmanager.backend.model.Task;
import com.taskmanager.backend.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceUnitTest {

    private TaskRepository taskRepository;
    private TaskService taskService;
    private Task testTask;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
        testTask = Task.builder().id(1L).name("Original Task").done(false).build();
    }

    @Test
    void updateTask_ShouldUpdateProperties() {
        when(taskRepository.findById(testTask.getId())).thenReturn(Optional.of(testTask));

        Task result = taskService.updateTask(testTask);

        assertNotNull(result);
        assertEquals(testTask.getName(), result.getName());
        assertEquals(testTask.isDone(), result.isDone());
        verify(taskRepository).findById(testTask.getId());
    }

    @Test
    void updateTask_ShouldThrowEntityNotFoundException() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(
                EntityNotFoundException.class,
                () -> taskService.updateTask(testTask),
                "Expected EntityNotFoundException to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Task not found with id"));
    }
}
