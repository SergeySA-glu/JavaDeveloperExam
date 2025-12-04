package com.tasktracker.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskGroupDTO {
    private Long id;
    private String name;
    private String createdAt;
}
