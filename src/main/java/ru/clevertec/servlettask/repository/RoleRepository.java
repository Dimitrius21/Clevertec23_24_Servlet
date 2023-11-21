package ru.clevertec.servlettask.repository;

import ru.clevertec.servlettask.entity.Role;

import java.util.HashMap;
import java.util.Map;

public class RoleRepository implements CRUDRepository<Role> {


    private static Long id = 0L;

    private static Map<Long, Role> roles = new HashMap<>();

    {
        roles.put(1L, new Role(1L, "USER"));
        roles.put(2L, new Role(2L, "ADMIN"));
        id = 2L;
    }

    @Override
    public Role create(Role role) {
        role.setId(++id);
        roles.put(id, role);
        return roles.get(id);
    }

    @Override
    public Role update(Role role) {
        roles.put(role.getId(), role);
        return role;
    }

    @Override
    public Role getById(Long id) {
        return roles.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return roles.remove(id) != null;
    }
}
