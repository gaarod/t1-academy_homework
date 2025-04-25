package org.t1academy.tasktracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.t1academy.tasktracker.entity.TaskStatus;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String title;
    private String description;
    private Long userId;
    private TaskStatus status;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TaskDto taskDto = (TaskDto) o;
        return Objects.equals(id, taskDto.id) && Objects.equals(title, taskDto.title) && Objects.equals(description, taskDto.description) && Objects.equals(userId, taskDto.userId) && status == taskDto.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, userId, status);
    }
}
