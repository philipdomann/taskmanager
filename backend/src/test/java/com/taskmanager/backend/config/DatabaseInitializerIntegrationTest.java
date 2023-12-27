package com.taskmanager.backend.config;

import com.taskmanager.backend.model.Task;
import com.taskmanager.backend.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DatabaseInitializerIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Test
    public void whenNoTasksInDb_thenOneSampleTaskShouldBeCreated() {
        taskRepository.deleteAll();
        databaseInitializer.run();
        assertEquals(1, taskRepository.count());
    }

    @Test
    public void whenPerformPerformanceTest_thenShouldCreateTenThousandTasks() {
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        databaseInitializer.run(new String[]{"--perform-performance-test=true"});

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository, times(10000)).save(taskCaptor.capture());
        List<Task> capturedTasks = taskCaptor.getAllValues();
        assertEquals(10000, capturedTasks.size());
    }
}
