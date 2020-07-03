package ru.otus.services;

import ru.otus.domain.User;

import java.util.List;

public interface UsersService {
    List<User> findAll();
    User findById(long id);
    User findByName(String name);
    User findRandom();
    User save(User user);
}
