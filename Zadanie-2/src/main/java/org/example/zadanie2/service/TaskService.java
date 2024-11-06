package org.example.zadanie2.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.zadanie2.dto.CreateTaskDto;
import org.example.zadanie2.dto.TaskDto;
import org.example.zadanie2.dto.UpdateTaskDto;
import org.example.zadanie2.mapper.TaskMapper;
import org.example.zadanie2.model.Task;
import org.example.zadanie2.model.TaskStatus;
import org.example.zadanie2.model.User;
import org.example.zadanie2.repository.TaskRepository;
import org.example.zadanie2.repository.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<TaskDto> getAllTasks(String title, String status, LocalDate dueDate, Long assignedUserId) {
        log.info("Fetching tasks with filters - title: {}, status: {}, dueDate: {}, assignedUserId: {}", title, status, dueDate, assignedUserId);
        Specification<Task> specification = buildTaskSpecification(title, status, dueDate, assignedUserId);
        List<Task> tasks = taskRepository.findAll(specification);
        log.info("Found {} tasks matching the filters", tasks.size());
        return tasks.stream()
                .map(TaskMapper::taskToDto)
                .toList();
    }

    public TaskDto getTaskById(Long id) {
        log.info("Fetching task with id {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task with id " + id + " not found"));
        return TaskMapper.taskToDto(task);
    }

    @Transactional
    public TaskDto createTask(CreateTaskDto createTaskDto) {
        log.info("Creating a new task with title: {}", createTaskDto.getTitle());
        Set<User> assignedUsers = new HashSet<>(userRepository.findAllById(createTaskDto.getAssignedUserIds()));
        if (assignedUsers.size() != createTaskDto.getAssignedUserIds().size()) {
            log.warn("Invalid user IDs in task creation request");
            throw new IllegalArgumentException("Some user IDs are invalid or do not exist");
        }
        Task task = new Task();
        task.setTitle(createTaskDto.getTitle());
        task.setDescription(createTaskDto.getDescription());
        task.setDueDate(createTaskDto.getDueDate());
        task.setStatus(TaskStatus.valueOf(createTaskDto.getStatus().toUpperCase()));
        task.setAssignedUsers(assignedUsers);
        taskRepository.save(task);
        log.info("Task created with ID {}", task.getId());
        return TaskMapper.taskToDto(task);
    }

    @Transactional
    public TaskDto updateTask(Long taskId, UpdateTaskDto taskDto) {
        log.info("Updating task with id {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task with id: "+ taskId +" not found"));

        if (taskDto.getTitle() != null && !taskDto.getTitle().isBlank()) {
            task.setTitle(taskDto.getTitle());
            log.info("Updated title for task with id {}", taskId);
        }
        if (taskDto.getDescription() != null) {
            task.setDescription(taskDto.getDescription());
            log.info("Updated description for task with id {}", taskId);
        }
        if (taskDto.getDueDate() != null) {
            if (taskDto.getDueDate().isBefore(LocalDate.now())) {
                log.warn("Due date is in the past for task with id {}", taskId);
                throw new IllegalArgumentException("Due date must be in the present or future");
            }
            task.setDueDate(taskDto.getDueDate());
            log.info("Updated due date for task with id {}", taskId);
        }
        if (taskDto.getStatus() != null) {
            try {
                task.setStatus(TaskStatus.valueOf(taskDto.getStatus().toUpperCase()));
                log.info("Updated status for task with id {}", taskId);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid status provided for task with id {}", taskId);
                throw new IllegalArgumentException("Invalid status value");
            }
        }
        if (taskDto.getAssignedUserIds() != null) {
            Set<User> assignedUsers = new HashSet<>(userRepository.findAllById(taskDto.getAssignedUserIds()));
            if (assignedUsers.size() != taskDto.getAssignedUserIds().size()) {
                log.warn("Invalid user IDs in task update request for task with id {}", taskId);
                throw new IllegalArgumentException("Some user IDs are invalid or do not exist");
            }
            task.setAssignedUsers(assignedUsers);
            log.info("Updated assigned users for task with id {}", taskId);
        }
        return TaskMapper.taskToDto(task);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        log.info("Deleting task with id {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task with ID " + taskId + " not found"));

        taskRepository.delete(task);
        log.info("Task with id {} successfully deleted", taskId);
    }

    private Specification<Task> buildTaskSpecification(String title, String status, LocalDate dueDate, Long assignedUserId) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (title != null && !title.isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
                log.debug("Adding title filter with value: {}", title);
            }
            if (status != null && !status.isEmpty()) {
                try {
                    TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
                    predicates.add(builder.equal(root.get("status"), taskStatus));
                    log.debug("Adding status filter with value: {}", status);
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid status filter: {}", status);
                    throw new IllegalArgumentException("Invalid status: " + status);
                }
            }
            if (dueDate != null) {
                predicates.add(builder.equal(root.get("dueDate"), dueDate));
                log.debug("Adding dueDate filter with value: {}", dueDate);
            }
            if (assignedUserId != null) {
                Join<Task, User> join = root.join("assignedUsers");
                predicates.add(builder.equal(join.get("id"), assignedUserId));
                log.debug("Adding assignedUser filter with ID: {}", assignedUserId);
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
