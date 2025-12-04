package com.example.demo.model.data;

import com.example.demo.model.domain.User;

import java.util.List;

public interface UserRepository {
    List<User> listAll();

    User findById(int id);

    User saveUser(User user);
}
