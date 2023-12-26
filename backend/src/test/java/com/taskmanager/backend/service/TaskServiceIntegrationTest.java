package com.taskmanager.backend.service;

import com.taskmanager.backend.model.Task;
import com.taskmanager.backend.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class TaskServiceIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testOptimisticLocking() {
        Task task = Task.builder().name("Initial Task").priority(Task.Priority.LOW).build();
        task = taskRepository.save(task);

        Task taskTx1 = taskRepository.findById(task.getId())
                .orElseThrow(() -> new AssertionError("Task not found"));
        Task taskTx2 = taskRepository.findById(task.getId())
                .orElseThrow(() -> new AssertionError("Task not found"));

        taskTx1.setName("Updated in Tx1");
        taskRepository.save(taskTx1);

        taskTx2.setName("Updated in Tx2");
        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            taskRepository.save(taskTx2);
        });
    }
}

