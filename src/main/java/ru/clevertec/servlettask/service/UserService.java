package ru.clevertec.servlettask.service;


import ru.clevertec.servlettask.entity.Role;
import ru.clevertec.servlettask.entity.User;
import ru.clevertec.servlettask.exception.IncorrectRequestDataException;
import ru.clevertec.servlettask.repository.RoleRepository;
import ru.clevertec.servlettask.repository.UserRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User createUser(User user) {
        Set<Role> roles = user.getRoles();
        if (isRolesInRequestCorrect(roles)) {
            return userRepository.create(user);
        } else {
            throw new IncorrectRequestDataException("Roles are not correct");
        }
    }

    public User updateUser(User user) {
        User user1 = userRepository.getById(user.getId());
        if (Objects.isNull(user1)) {
            throw new IncorrectRequestDataException("User with id " + user.getId() + "has not found");
        }
        user1.setName(user.getName());
        Set<Role> roles = user.getRoles();
        if (isRolesInRequestCorrect(roles)) {
            user1.setRoles(roles);
        } else {
            throw new IncorrectRequestDataException("Roles are not correct");
        }
        return userRepository.update(user1);
    }

    public boolean deleteUser(Long id) {
        return userRepository.deleteById(id);
    }

    public User getUser(Long id) {
        User user = userRepository.getById(id);
        if (Objects.isNull(user)) {
            throw new IncorrectRequestDataException("User has not found");
        }
        return user;
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.getUserByName(username);
    }

    private boolean isRolesInRequestCorrect(Set<Role> roles) {
        return roles.stream().allMatch(r -> {
            long id = r.getId();
            Role role = roleRepository.getById(id);
            return Objects.nonNull(role) && role.getRole().equals(r.getRole());
        });
    }
}
