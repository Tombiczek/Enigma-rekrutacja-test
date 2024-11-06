package org.example.zadanie2.mapper;

import org.example.zadanie2.dto.UserDto;
import org.example.zadanie2.model.Task;
import org.example.zadanie2.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto userToDto(User user) {
        Set<Long> taskIds = user.getTasks().stream()
                .map(Task::getId)
                .collect(Collectors.toSet());

        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .taskIds(taskIds)
                .build();
    }
}
