package org.t1academy.tasktracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
}
