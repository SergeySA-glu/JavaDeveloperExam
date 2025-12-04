package com.tasktracker.model.data;

import com.tasktracker.model.domain.User;

import java.util.List;

public interface UserRepository {
    List<User> listAll();

    User findById(int id);

    User saveUser(User user);
}
