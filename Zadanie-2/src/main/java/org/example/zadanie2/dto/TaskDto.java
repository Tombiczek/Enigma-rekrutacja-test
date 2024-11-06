package org.example.zadanie2.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class TaskDto {

    private Long id;
    private String title;
    private String description;
    private String status;
    private LocalDate dueDate;
    private Set<Long> assignedUserIds;
}
