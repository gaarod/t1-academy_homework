package org.t1academy.tasktracker.dto;

import lombok.*;
import org.t1academy.tasktracker.entity.TaskStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskNotificationDto {
    private Long id;
    private TaskStatus status;
}
