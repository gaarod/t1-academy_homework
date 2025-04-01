package org.t1academy.tasktracker.controller;

import org.springframework.web.bind.annotation.*;
import org.t1academy.tasktracker.dto.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.t1academy.tasktracker.service.TaskService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto createTask(@RequestBody TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping
    public List<TaskDto> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
    }

}
