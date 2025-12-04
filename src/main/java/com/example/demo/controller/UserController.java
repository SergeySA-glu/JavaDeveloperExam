package com.example.demo.controller;

import com.example.demo.model.domain.User;
import com.example.demo.model.domain.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-api")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/user")
    public List<User> listUsers() {
        return service.listUsers();
    }

    @GetMapping("/user/{id}")
    public User findUser(@PathVariable(name = "id") int index) {
        return service.findUser(index);
    }

    @PostMapping(value = "/user")
    public User createUser(@RequestBody User user) {
        return service.createUser(user);
    }
}
