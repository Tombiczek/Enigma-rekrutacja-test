package org.example.zadanie2.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.zadanie2.dto.CreateUserDto;
import org.example.zadanie2.dto.UserDto;
import org.example.zadanie2.mapper.UserMapper;
import org.example.zadanie2.model.User;
import org.example.zadanie2.repository.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> getAllUsers(String firstName, String lastName, String email) {
        log.info("Fetching users with filters - firstName: {}, lastName: {}, email: {}", firstName, lastName, email);
        Specification<User> specification = buildUserSpecification(firstName, lastName, email);
        List<User> users = userRepository.findAll(specification);
        log.info("Found {} users matching the filters", users.size());
        return users.stream()
                .map(UserMapper::userToDto)
                .toList();
    }

    public UserDto getUserById(Long id) {
        log.info("Fetching user with id {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        return UserMapper.userToDto(user);
    }

    @Transactional
    public UserDto createUser(CreateUserDto createUserDto) {
        log.info("Creating a new user with email: {}", createUserDto.getEmail());
        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            log.warn("Attempted to create a user with an existing email: {}", createUserDto.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }
        User user = new User();
        user.setFirstName(createUserDto.getFirstName());
        user.setLastName(createUserDto.getLastName());
        user.setEmail(createUserDto.getEmail());
        User savedUser = userRepository.save(user);
        log.info("User created with id {}", savedUser.getId());
        return UserMapper.userToDto(savedUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        log.info("Deleting user with id {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

        userRepository.delete(user);
        log.info("User with id {} successfully deleted", userId);
    }

    private Specification<User> buildUserSpecification(String firstName, String lastName, String email) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (firstName != null && !firstName.isEmpty()) {
                predicates.add(builder.like(
                        builder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"));
                log.debug("Added filter for firstName: {}", firstName);
            }

            if (lastName != null && !lastName.isEmpty()) {
                predicates.add(builder.like(
                        builder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%"));
                log.debug("Added filter for lastName: {}", lastName);
            }

            if (email != null && !email.isEmpty()) {
                predicates.add(builder.equal(
                        builder.lower(root.get("email")), email.toLowerCase()));
                log.debug("Added filter for email: {}", email);
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
