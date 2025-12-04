package com.example.demo.model.domain;

import java.util.List;

public interface UserService {
    List<User> listUsers();

    User findUser(int id);

    User createUser(User user);
}
