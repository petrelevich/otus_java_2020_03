package ru.otus.repostory;

import org.springframework.stereotype.Repository;
import ru.otus.domain.User;
import ru.otus.generators.UserIdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final List<User> users = new ArrayList<>();
    private final UserIdGenerator userIdGenerator;

    public UserRepositoryImpl(UserIdGenerator userIdGenerator) {
        this.userIdGenerator = userIdGenerator;

        users.add(new User(this.userIdGenerator.getUserId(), "Крис Гир"));
        users.add(new User(this.userIdGenerator.getUserId(), "Ая Кэш"));
        users.add(new User(this.userIdGenerator.getUserId(), "Десмин Боргес"));
        users.add(new User(this.userIdGenerator.getUserId(), "Кетер Донохью"));
        users.add(new User(this.userIdGenerator.getUserId(), "Стивен Шнайдер"));
        users.add(new User(this.userIdGenerator.getUserId(), "Джанет Вэрни"));
        users.add(new User(this.userIdGenerator.getUserId(), "Брэндон Смит"));
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User save(User user) {
        user.setId(userIdGenerator.getUserId());
        users.add(user);
        return user;
    }

    @Override
    public User findById(long id) {
        return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    @Override
    public User findByName(String name) {
        return users.stream().filter(u -> u.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
