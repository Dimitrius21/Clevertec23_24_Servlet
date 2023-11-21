package ru.clevertec.servlettask.service;


import ru.clevertec.servlettask.entity.Role;
import ru.clevertec.servlettask.exception.IncorrectRequestDataException;
import ru.clevertec.servlettask.repository.RoleRepository;

import java.util.Objects;

public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(Role role) {
        role.setRole(role.getRole().toUpperCase());
        return roleRepository.create(role);
    }

    public Role updateRole(Role role) {
        Role roleInPersistence = roleRepository.getById(role.getId());
        if (Objects.isNull(roleInPersistence)){
            throw new IncorrectRequestDataException("Role with id " + role.getId() + "has not found");
        }
        roleInPersistence.setRole(role.getRole().toUpperCase());
        return roleRepository.update(roleInPersistence);
    }

    public boolean deleteRole(Long id) {
        return roleRepository.deleteById(id);
    }

    public Role getRole(Long id) {
        Role role = roleRepository.getById(id);
        if (Objects.isNull(role)) {
            throw new IncorrectRequestDataException("Role with such id has not found");
        }
        return role;
    }
}
