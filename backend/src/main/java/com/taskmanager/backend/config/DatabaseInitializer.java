package com.taskmanager.backend.config;

import com.taskmanager.backend.model.Task;
import com.taskmanager.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final TaskRepository taskRepository;
    private final Environment environment;
    public static final String CREATE_SAMPLE_TASK = "create-sample-task";
    public static final String PERFORM_PERFORMANCE_TEST = "perform-performance-test";

    @Autowired
    public DatabaseInitializer(TaskRepository taskRepository, Environment environment) {
        this.taskRepository = taskRepository;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        boolean performPerformanceTest = Arrays.asList(args).contains("--perform-performance-test=true");

        createSampleTaskIfNecessary();
        runPerformanceTestIfRequested(performPerformanceTest);
    }

    private void createSampleTaskIfNecessary() {
        if (Boolean.parseBoolean(environment.getProperty(CREATE_SAMPLE_TASK)) && taskRepository.count() == 0) {
            Task exampleTask = Task.builder()
                    .name("Example Task")
                    .priority(Task.Priority.NORMAL)
                    .done(false)
                    .build();
            taskRepository.save(exampleTask);
            System.out.println("Example Task created");
        }
    }

    private void runPerformanceTestIfRequested(boolean performPerformanceTest) {
        if (Boolean.parseBoolean(environment.getProperty(PERFORM_PERFORMANCE_TEST)) || performPerformanceTest) {
            for (int i = 0; i < 10000; i++) {
                Task task = Task.builder()
                        .name("Task " + i)
                        .priority(i % 2 == 0 ? Task.Priority.URGENT : Task.Priority.LOW)
                        .done(i % 3 == 0)
                        .build();
                taskRepository.save(task);
            }
            System.out.println("10,000 tasks for performance testing created");
        }
    }
}
