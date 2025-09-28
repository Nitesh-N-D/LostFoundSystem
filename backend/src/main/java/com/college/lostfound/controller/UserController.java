package com.college.lostfound.controller;

import com.college.lostfound.model.User;
import com.college.lostfound.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return repo.save(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User loginData) {
        User user = repo.findByEmail(loginData.getEmail());
        if (user != null && user.getPassword().equals(loginData.getPassword())) {
            return user;
        }
        throw new RuntimeException("Invalid login");
    }

    @GetMapping
    public List<User> getAll() {
        return repo.findAll();
    }
}
