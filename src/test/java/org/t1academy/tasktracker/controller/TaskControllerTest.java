package org.t1academy.tasktracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.t1academy.tasktracker.dto.TaskDto;
import org.t1academy.tasktracker.entity.Task;
import org.t1academy.tasktracker.entity.TaskStatus;
import org.t1academy.tasktracker.repository.TaskRepository;
import org.t1academy.tasktracker.service.TaskService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.t1academy.tasktracker.utils.TaskBuilder.getTask;
import static org.t1academy.tasktracker.utils.TaskBuilder.getTaskDto;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest extends TestContainers {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    TaskService taskService;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        saveTestTasks();
    }

    @Test
    void createTask_ShouldSaveAndReturnTask() throws Exception {
        TaskDto taskDto = getTaskDto(null, 1L, TaskStatus.TO_DO);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title").value(taskDto.getTitle()))
                .andExpect(jsonPath("description").value(taskDto.getDescription()))
                .andExpect(jsonPath("userId").value(taskDto.getUserId()))
                .andExpect(jsonPath("status").value(taskDto.getStatus().name()));
    }

    @Test
    void getTaskById_ShouldReturnTask() throws Exception {
        TaskDto taskDto = getTaskDto(1L, 1L, TaskStatus.TO_DO);
        taskDto = taskService.createTask(taskDto);

        mockMvc.perform(get("/tasks/" + taskDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(taskDto.getId()))
                .andExpect(jsonPath("title").value(taskDto.getTitle()))
                .andExpect(jsonPath("description").value(taskDto.getDescription()))
                .andExpect(jsonPath("userId").value(taskDto.getUserId()))
                .andExpect(jsonPath("status").value(taskDto.getStatus().name()));
    }

    @Test
    void getTaskById_ShouldReturn404_WhenTaskDoesNotExist() throws Exception {
        Long taskId = 100L;
        mockMvc.perform(get("/tasks/" + taskId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllTasks_ShouldReturnListOfAllTasks() throws Exception {

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void updateTask_ShouldUpdateTask() throws Exception {
        TaskDto taskUpdateRequest = getTaskDto(2L, 1L, TaskStatus.IN_PROGRESS);

        mockMvc.perform(put("/tasks/" + taskUpdateRequest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(taskUpdateRequest.getId()))
                .andExpect(jsonPath("title").value(taskUpdateRequest.getTitle()))
                .andExpect(jsonPath("description").value(taskUpdateRequest.getDescription()))
                .andExpect(jsonPath("userId").value(taskUpdateRequest.getUserId()))
                .andExpect(jsonPath("status").value(taskUpdateRequest.getStatus().name()));
    }

    @Test
    void updateTask_ShouldReturn404_WhenTaskDoesNotExist() throws Exception {
        Long taskId = 100L;
        TaskDto taskUpdateRequest = getTaskDto(taskId, 1L, TaskStatus.IN_PROGRESS);

        mockMvc.perform(put("/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskUpdateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_ShouldDeleteTask() throws Exception {
        TaskDto taskDto = getTaskDto(1L, 1L, TaskStatus.TO_DO);
        taskDto = taskService.createTask(taskDto);

        mockMvc.perform(delete("/tasks/" + taskDto.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTask_ShouldReturn404_WhenTaskDoesNotExist() throws Exception {
        Long taskId = 100L;

        mockMvc.perform(delete("/tasks/" + taskId))
                .andExpect(status().isNotFound());
    }



    private void saveTestTasks() {
        Task task1 = getTask(1L, 1L, TaskStatus.TO_DO);
        Task task2 = getTask(2L, 2L, TaskStatus.IN_PROGRESS);
        Task task3 = getTask(3L, 3L, TaskStatus.TO_DO);

        taskRepository.saveAll(List.of(task1, task2, task3));
    }


}