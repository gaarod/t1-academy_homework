package org.t1academy.tasktracker.service;

import org.t1academy.tasktracker.dto.TaskNotificationDto;

public interface TaskStatusProducer {

    void sendTaskStatusUpdate(TaskNotificationDto taskNotificationDto);
}
