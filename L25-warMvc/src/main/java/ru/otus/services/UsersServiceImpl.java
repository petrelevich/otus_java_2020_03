package ru.otus.services;

import org.springframework.stereotype.Service;
import ru.otus.domain.User;
import ru.otus.repostory.UserRepository;

import java.util.List;
import java.util.Random;

@Service
public class UsersServiceImpl implements UsersService {

    private final UserRepository userRepository;

    public UsersServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public User findRandom() {
        List<User> users = userRepository.findAll();
        Random r = new Random();
        return users.stream().skip(r.nextInt(users.size() - 1)).findFirst().orElse(null);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
