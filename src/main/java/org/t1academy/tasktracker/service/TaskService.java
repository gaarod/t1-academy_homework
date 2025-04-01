package org.t1academy.tasktracker.service;

import org.t1academy.tasktracker.dto.TaskDto;

import java.util.List;

public interface TaskService {

    TaskDto getTaskById(Long id);
    List<TaskDto> getAllTasks();
    TaskDto createTask(TaskDto taskDto);
    TaskDto updateTask(Long id, TaskDto taskDto);
    void deleteTaskById(Long id);
}
