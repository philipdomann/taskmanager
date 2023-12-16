package com.taskmanager.backend.service;

import com.taskmanager.backend.model.Task;
import com.taskmanager.backend.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> getTask(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Transactional
    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id " + id));

        existingTask.setName(updatedTask.getName());
        existingTask.setDone(updatedTask.isDone());
        existingTask.setPriority(updatedTask.getPriority());
        return existingTask;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}

