package com.taskmanager.backend.config;

import com.taskmanager.backend.model.Task;
import com.taskmanager.backend.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DatabaseInitializerTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private Environment environment;

    @InjectMocks
    private DatabaseInitializer databaseInitializer;

    @Test
    public void whenCreateSampleTaskEnabledAndNoTasks_thenCreateSampleTask() {
        when(environment.getProperty(DatabaseInitializer.CREATE_SAMPLE_TASK)).thenReturn("true");
        when(taskRepository.count()).thenReturn(0L);
        when(taskRepository.save(any(Task.class))).thenReturn(new Task());

        databaseInitializer.run();

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void whenPerformPerformanceTest_thenShouldCreateTenThousandTasks() {
        when(environment.getProperty(DatabaseInitializer.PERFORM_PERFORMANCE_TEST)).thenReturn("true");
        when(environment.getProperty(DatabaseInitializer.CREATE_SAMPLE_TASK)).thenReturn("false");
        when(taskRepository.save(any(Task.class))).thenReturn(new Task());

        databaseInitializer.run("--perform-performance-test=true");

        verify(taskRepository, times(10000)).save(any(Task.class));
    }
}
