package com.taskmanager.backend.service;

import com.taskmanager.backend.model.Task;
import com.taskmanager.backend.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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
    public Task updateTask(Task updatedTask) {
        try {
            Task existingTask = taskRepository.findById(updatedTask.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Task not found with id " + updatedTask.getId()));

            existingTask.setName(updatedTask.getName());
            existingTask.setDone(updatedTask.isDone());
            existingTask.setPriority(updatedTask.getPriority());

            return taskRepository.save(existingTask);
        } catch (OptimisticLockException e) {
            Task conflictTask = taskRepository.findById(updatedTask.getId())
                    .orElseThrow(() -> new NoSuchElementException("Task not found with id " + updatedTask.getId()));
            throw new TaskUpdateConflictException(conflictTask);
        }
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}

