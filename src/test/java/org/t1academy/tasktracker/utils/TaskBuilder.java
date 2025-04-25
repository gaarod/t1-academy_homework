package org.t1academy.tasktracker.utils;

import org.t1academy.tasktracker.dto.TaskDto;
import org.t1academy.tasktracker.entity.Task;
import org.t1academy.tasktracker.entity.TaskStatus;

public class TaskBuilder {

    public static Task getTask(Long id, Long userId, TaskStatus status) {
        return new Task(id, "test", "test description", userId, status);
    }

    public static TaskDto getTaskDto(Long id, Long userId, TaskStatus status) {
        return new TaskDto(id, "test", "test description", userId, status);
    }
}
