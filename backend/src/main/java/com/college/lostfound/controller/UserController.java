package com.college.lostfound.controller;

import com.college.lostfound.model.User;
import com.college.lostfound.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){ this.userService = userService; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body){
        String email = body.get("email");
        if(userService.findByEmail(email).isPresent()){
            return ResponseEntity.badRequest().body(Map.of("error","Email already exists"));
        }
        User u = User.builder()
                .email(email)
                .name(body.get("name"))
                .password(body.get("password")) // NOTE: store hashed passwords in prod
                .role("ROLE_USER")
                .phone(body.get("phone"))
                .department(body.get("department"))
                .build();
        userService.save(u);
        return ResponseEntity.ok(Map.of("message","registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body){
        String email = body.get("email");
        String password = body.get("password");
        return userService.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .map(u -> ResponseEntity.ok(Map.of("message","ok","user",u)))
                .orElse(ResponseEntity.status(401).body(Map.of("error","Invalid credentials")));
    }
}
