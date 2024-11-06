package org.example.zadanie2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zadanie2.dto.CreateTaskDto;
import org.example.zadanie2.dto.TaskDto;
import org.example.zadanie2.dto.UpdateTaskDto;
import org.example.zadanie2.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing tasks.
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Retrieves a list of tasks, filtered by the provided criteria.
     * The title filter uses "like" matching, while status, due date, and assigned user use exact matching.
     *
     * @param title         the title to filter by (optional, uses "like" matching)
     * @param status        the status to filter by (optional, uses exact matching)
     * @param dueDate       the due date to filter by (optional, uses exact matching)
     * @param assignedUserId the assigned user ID to filter by (optional, uses exact matching)
     * @return a list of TaskDto objects matching the filter criteria
     */
    @GetMapping
    public List<TaskDto> getAllTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate dueDate,
            @RequestParam(required = false) Long assignedUserId
    ) {
        return taskService.getAllTasks(title, status, dueDate, assignedUserId);
    }

    /**
     * Retrieves a task by its unique identifier.
     *
     * @param taskId the ID of the task to retrieve
     * @return the TaskDto of the specified task
     */
    @GetMapping("/{taskId}")
    public TaskDto getTaskById(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId);
    }

    /**
     * Creates a new task. The input data is validated based on constraints defined in CreateTaskDto.
     *
     * @param taskDto the data transfer object containing the details of the task to create
     * @return the created TaskDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto createTask(@Valid @RequestBody CreateTaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    /**
     * Updates an existing task. Allows partial updates, meaning any field can be updated.
     * The input values are validated within the service layer.
     *
     * @param taskId the ID of the task to update
     * @param taskDto the data transfer object containing the fields to update
     * @return the updated TaskDto
     */
    @PatchMapping("/{taskId}")
    public TaskDto editTask(@PathVariable Long taskId, @RequestBody UpdateTaskDto taskDto) {
        return taskService.updateTask(taskId, taskDto);
    }

    /**
     * Deletes a task by its unique identifier.
     *
     * @param taskId the ID of the task to delete
     */
    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }
}
