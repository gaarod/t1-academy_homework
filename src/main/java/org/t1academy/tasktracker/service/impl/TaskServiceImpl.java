package org.t1academy.tasktracker.service.impl;

import org.t1academy.logstarter.aspect.annotation.HandlingResult;
import org.t1academy.logstarter.aspect.annotation.LogException;
import org.t1academy.logstarter.aspect.annotation.LogExecution;
import org.t1academy.logstarter.aspect.annotation.LogTracking;
import org.t1academy.tasktracker.dto.TaskDto;
import org.t1academy.tasktracker.dto.TaskNotificationDto;
import org.t1academy.tasktracker.entity.Task;
import org.t1academy.tasktracker.entity.TaskStatus;
import org.t1academy.tasktracker.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.t1academy.tasktracker.mapper.TaskMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.t1academy.tasktracker.repository.TaskRepository;
import org.t1academy.tasktracker.service.TaskStatusProducer;
import org.t1academy.tasktracker.service.TaskService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskStatusProducer taskStatusProducer;


    @HandlingResult
    @LogExecution
    @Override
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(
                () -> new AppException("Task not found", HttpStatus.NOT_FOUND)
        );

        return taskMapper.toDto(task);
    }

    @HandlingResult
    @LogExecution
    @Override
    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @HandlingResult
    @LogExecution
    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task savedTask = taskRepository.save(task);

        return taskMapper.toDto(savedTask);
    }

    @HandlingResult
    @LogExecution
    @Override
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(
                        () -> new AppException("Task not found", HttpStatus.NOT_FOUND)
                );
        TaskStatus oldStatus = task.getStatus();

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setUserId(taskDto.getUserId());
        task.setStatus(taskDto.getStatus());
        Task savedTask = taskRepository.save(task);

        if (oldStatus != savedTask.getStatus()) {
            taskStatusProducer.sendTaskStatusUpdate(new TaskNotificationDto(id, savedTask.getStatus()));
        }

        return taskMapper.toDto(savedTask);
    }

    @HandlingResult
    @LogExecution
    @Override
    public void deleteTaskById(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new AppException("Task not found", HttpStatus.NOT_FOUND);
        }
    }
}
