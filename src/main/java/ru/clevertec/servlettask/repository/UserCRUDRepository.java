package ru.clevertec.servlettask.repository;

import ru.clevertec.servlettask.entity.User;

import java.util.Optional;

public interface UserCRUDRepository extends CRUDRepository<User> {
    public Optional<User> getUserByName(String userName);
}
