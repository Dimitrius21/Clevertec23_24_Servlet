package ru.clevertec.servlettask.repository;

public interface CRUDRepository<T> {

    T create(T t);

    T update(T t);

    T getById(Long id);

    boolean deleteById(Long id);
}
