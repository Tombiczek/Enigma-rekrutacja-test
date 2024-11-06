package org.example.zadanie2.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UpdateTaskDto {
    private String title;
    private String description;
    private String status;
    private LocalDate dueDate;
    private Set<Long> assignedUserIds;
}
