package com.taskmanager.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.taskmanager.backend.model.Task;
import com.taskmanager.backend.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class TaskControllerMvcTest {

    @Mock
    private TaskService taskService;
    @InjectMocks
    private TaskController taskController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.
                standaloneSetup(taskController)
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void postTask_WithEmptyName_ShouldReturnBadRequest() throws Exception {
        Task invalidTask = new Task();
        invalidTask.setName("");

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTask)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void postTask_WithValidData_ShouldCreateTaskAndReturnOk() throws Exception {
        Task validTask = new Task();
        validTask.setName("Valid Task Name");
        validTask.setPriority(Task.Priority.MEDIUM);
        when(taskService.createTask(any(Task.class))).thenReturn(validTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Valid Task Name"))
                .andExpect(jsonPath("$.priority").value(Task.Priority.MEDIUM.toString()));
    }

    @Test
    void whenPostRequestWithoutPriority_thenValidationError() throws Exception {
        Task taskWithoutPriority = new Task();
        taskWithoutPriority.setName("Valid Name");

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskWithoutPriority)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void whenPostRequestWithTooLongName_thenValidationError() throws Exception {
        Task taskWithLongName = new Task();
        taskWithLongName.setName("a".repeat(201));
        taskWithLongName.setPriority(Task.Priority.MEDIUM);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskWithLongName)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void whenPutRequestWithEmptyName_thenValidationError() throws Exception {
        Task taskWithEmptyName = new Task();
        taskWithEmptyName.setId(1L);
        taskWithEmptyName.setName("");
        taskWithEmptyName.setPriority(Task.Priority.MEDIUM);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskWithEmptyName)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void whenPutRequestWithNullPriority_thenValidationError() throws Exception {
        Task taskWithNullPriority = new Task();
        taskWithNullPriority.setId(1L);
        taskWithNullPriority.setName("Valid Name");
        taskWithNullPriority.setPriority(null);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskWithNullPriority)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void whenGetRequestForExistingTask_thenRetrieveTask() throws Exception {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setName("Existing Task");
        existingTask.setPriority(Task.Priority.MEDIUM);
        when(taskService.getTask(1L)).thenReturn(Optional.of(existingTask));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Existing Task"))
                .andExpect(jsonPath("$.priority").value(Task.Priority.MEDIUM.toString()));
    }

    @Test
    void whenGetRequestForNonExistingTask_thenNotFound() throws Exception {
        when(taskService.getTask(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteRequestForExistingTask_thenDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk());
        verify(taskService).deleteTask(1L);
    }

    @Test
    void whenDeleteRequestForNonExistingTask_thenNotFound() throws Exception {
//        doThrow(new EntityNotFoundException("Task not found")).when(taskService).deleteTask(anyLong());

        mockMvc.perform(delete("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetRequestForAllTasks_thenRetrieveTasks() throws Exception {
        List<Task> allTasks = Arrays.asList(new Task(), new Task());
        when(taskService.getAllTasks()).thenReturn(allTasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(allTasks.size()));
    }

}
