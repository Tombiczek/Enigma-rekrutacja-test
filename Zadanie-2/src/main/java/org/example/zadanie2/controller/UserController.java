package org.example.zadanie2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zadanie2.dto.CreateUserDto;
import org.example.zadanie2.dto.UserDto;
import org.example.zadanie2.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves a list of users, filtered by the provided criteria.
     * Name and last name filters use "like" matching, while the email filter uses exact matching.
     *
     * @param firstName the first name to filter by (optional, uses "like" matching)
     * @param lastName  the last name to filter by (optional, uses "like" matching)
     * @param email     the email to filter by (optional, uses exact matching)
     * @return a list of UserDto objects matching the filter criteria
     */
    @GetMapping
    public List<UserDto> getAllUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email) {

        return userService.getAllUsers(firstName, lastName, email);
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId the ID of the user to retrieve
     * @return the UserDto of the specified user
     */
    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    /**
     * Creates a new user. The input data is validated based on constraints defined in CreateUserDto.
     *
     * @param createUserDto the data transfer object containing the details of the user to create
     * @return the created UserDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        return userService.createUser(createUserDto);
    }

    /**
     * Deletes a user by their unique identifier.
     *
     * @param userId the ID of the user to delete
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
