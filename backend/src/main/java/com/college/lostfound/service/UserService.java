package com.college.lostfound.service;

import com.college.lostfound.model.User;
import com.college.lostfound.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo){ this.repo = repo; }

    public Optional<User> findByEmail(String email){ return repo.findByEmail(email); }
    public User save(User u){ return repo.save(u); }
}
