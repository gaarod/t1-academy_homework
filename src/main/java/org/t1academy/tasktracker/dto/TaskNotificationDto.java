package org.t1academy.tasktracker.dto;

import lombok.*;
import org.t1academy.tasktracker.entity.TaskStatus;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskNotificationDto {
    private Long id;
    private TaskStatus status;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TaskNotificationDto that = (TaskNotificationDto) o;
        return Objects.equals(id, that.id) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }
}
