package com.example.demo.model.data;

import com.example.demo.model.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final List<User> users = new ArrayList<>(List.of(
            new User("a", "b"),
            new User("x", "y"),
            new User("123", "456")
    ));

    @Override
    public List<User> listAll() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public User findById(int id) {
        return users.get(id);
    }

    @Override
    public User saveUser(User user) {
        users.add(user);
        return user;
    }
}
