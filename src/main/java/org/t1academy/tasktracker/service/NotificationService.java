package org.t1academy.tasktracker.service;

import org.t1academy.tasktracker.dto.TaskNotificationDto;

public interface NotificationService {

    void notifyTaskStatusUpdate(TaskNotificationDto taskNotificationDto);
}
