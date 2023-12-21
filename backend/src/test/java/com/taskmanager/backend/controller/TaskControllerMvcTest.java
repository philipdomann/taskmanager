package com.taskmanager.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.taskmanager.backend.model.Task;
import com.taskmanager.backend.service.TaskService;
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
    void postTask_WithValidData_ShouldCreateTaskAndReturnOk() throws Exception {
        Task validTask = Task.builder().name("Valid Task Name").priority(Task.Priority.MEDIUM).build();
        when(taskService.createTask(any(Task.class))).thenReturn(validTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Valid Task Name"))
                .andExpect(jsonPath("$.priority").value(Task.Priority.MEDIUM.toString()));
    }

    @Test
    void postTask_WithoutPriority_thenValidationError() throws Exception {
        Task taskWithoutPriority = Task.builder().name("Valid Task Name").build();

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskWithoutPriority)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void postTask_WithEmptyName_ShouldReturnBadRequest() throws Exception {
        Task taskWithEmptyName = Task.builder().name("").priority(Task.Priority.MEDIUM).build();

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskWithEmptyName)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void postTask_WithTooLongName_thenValidationError() throws Exception {
        Task taskWithLongName = Task.builder().name("a".repeat(201)).priority(Task.Priority.MEDIUM).build();

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskWithLongName)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void putTask_WithValidData_ShouldUpdateTaskAndReturnOk() throws Exception {
        Task validTask = Task.builder().id(1L).name("Valid Task Name").priority(Task.Priority.MEDIUM).build();
        when(taskService.updateTask(any(Task.class))).thenReturn(validTask);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validTask.getId()))
                .andExpect(jsonPath("$.name").value("Valid Task Name"))
                .andExpect(jsonPath("$.priority").value(Task.Priority.MEDIUM.toString()));
    }


    @Test
    void putTask_WithEmptyName_thenValidationError() throws Exception {
        Task taskWithEmptyName = Task.builder().id(1L).name("").priority(Task.Priority.MEDIUM).build();

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskWithEmptyName)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void putTask_WithNullPriority_thenValidationError() throws Exception {
        Task taskWithNullPriority = Task.builder().id(1L).name("Valid Name").priority(null).build();

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskWithNullPriority)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void putTask_WithTooLongName_thenValidationError() throws Exception {
        Task taskWithLongName = Task.builder().id(1L).name("a".repeat(201)).priority(Task.Priority.MEDIUM).build();

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskWithLongName)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void getTask_ExistingTask_thenRetrieveTask() throws Exception {
        Task existingTask = Task.builder().id(1L).name("Existing Task").priority(Task.Priority.MEDIUM).build();
        when(taskService.getTask(existingTask.getId())).thenReturn(Optional.of(existingTask));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingTask.getId()))
                .andExpect(jsonPath("$.name").value("Existing Task"))
                .andExpect(jsonPath("$.priority").value(Task.Priority.MEDIUM.toString()));
    }

    @Test
    void getTask_AllTasks_thenRetrieveTasks() throws Exception {
        List<Task> allTasks = Arrays.asList(new Task(), new Task());
        when(taskService.getAllTasks()).thenReturn(allTasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(allTasks.size()));
    }

    @Test
    void getTask_NonExistingTask_thenNotFound() throws Exception {
        when(taskService.getTask(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_ExistingTask_thenDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk());
        verify(taskService).deleteTask(1L);
    }

    @Test
    void deleteTask_NonExistingTask_thenIsOk() throws Exception {
        mockMvc.perform(delete("/api/tasks/999"))
                .andExpect(status().isOk());
    }

}
