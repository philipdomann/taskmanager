package com.taskmanager.backend.controller;

import com.taskmanager.backend.model.Task;
import com.taskmanager.backend.service.TaskService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public Task createTask(@Valid @RequestBody Task task) {
        return taskService.createTask(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable(value = "id") Long taskId) {
        return taskService.getTask(taskId)
                .map(task -> ResponseEntity.ok().body(task))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@Valid @RequestBody Task taskDetails) {
        try {
            return ResponseEntity.ok(taskService.updateTask(taskDetails));
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable(value = "id") Long taskId) {
        try {
            taskService.deleteTask(taskId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }
}

