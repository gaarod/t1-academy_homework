package org.t1academy.tasktracker.service.impl;

import org.t1academy.tasktracker.dto.TaskDto;
import org.t1academy.tasktracker.entity.Task;
import org.t1academy.tasktracker.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.t1academy.tasktracker.mapper.TaskMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.t1academy.tasktracker.repository.TaskRepository;
import org.t1academy.tasktracker.service.TaskService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;


    @Override
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(
                () -> new AppException("Task not found", HttpStatus.NOT_FOUND)
        );

        return taskMapper.toDto(task);
    }

    @Override
    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task savedTask = taskRepository.save(task);

        return taskMapper.toDto(savedTask);
    }

    @Override
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(
                        () -> new AppException("Task not found", HttpStatus.NOT_FOUND)
                );

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setUserId(taskDto.getUserId());
        Task savedTask = taskRepository.save(task);

        return taskMapper.toDto(savedTask);
    }

    @Override
    public void deleteTaskById(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new AppException("Task not found", HttpStatus.NOT_FOUND);
        }
    }
}
