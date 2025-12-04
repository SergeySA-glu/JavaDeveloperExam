package com.tasktracker.model.dto;

import com.tasktracker.model.entity.TaskStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long groupId;
    private String createdAt;
    private String updatedAt;
}
