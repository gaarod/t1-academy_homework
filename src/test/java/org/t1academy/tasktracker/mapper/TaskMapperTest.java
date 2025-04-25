package org.t1academy.tasktracker.mapper;

import org.junit.jupiter.api.Test;
import org.t1academy.tasktracker.dto.TaskDto;
import org.t1academy.tasktracker.entity.Task;
import org.t1academy.tasktracker.entity.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.t1academy.tasktracker.utils.TaskBuilder.getTask;
import static org.t1academy.tasktracker.utils.TaskBuilder.getTaskDto;

class TaskMapperTest {

    private final TaskMapper taskMapper = new TaskMapperImpl();

    @Test
    public void toEntity() {
        Task task = getTask(1L, 1L, TaskStatus.TO_DO);
        TaskDto taskDto = getTaskDto(1L, 1L, TaskStatus.TO_DO);

        Task result = taskMapper.toEntity(taskDto);

        assertEquals(task, result);
    }

    @Test
    public void toDto() {
        Task task = getTask(1L, 1L, TaskStatus.TO_DO);
        TaskDto taskDto = getTaskDto(1L, 1L, TaskStatus.TO_DO);

        TaskDto result = taskMapper.toDto(task);

        assertEquals(taskDto, result);
    }

}