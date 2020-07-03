package ru.otus.repostory;

import ru.otus.domain.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    User save(User user);

    User findById(long id);

    User findByName(String name);
}
