package com.tasktracker.model.domain;

import com.tasktracker.model.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> listUsers() {
        return repository.listAll();
    }

    @Override
    public User findUser(int id) {
        return repository.findById(id);
    }

    @Override
    public User createUser(User user) {
        return repository.saveUser(user);
    }
}
