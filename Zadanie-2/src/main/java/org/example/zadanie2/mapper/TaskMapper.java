package org.example.zadanie2.mapper;

import org.example.zadanie2.dto.TaskDto;
import org.example.zadanie2.model.Task;
import org.example.zadanie2.model.User;

import java.util.stream.Collectors;

public class TaskMapper {

    public static TaskDto taskToDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .dueDate(task.getDueDate())
                .assignedUserIds(task.getAssignedUsers().stream()
                        .map(User::getId)
                        .collect(Collectors.toSet()))
                .build();
    }
}
