package com.report.report_api_pattern.controller;

import com.report.report_api_pattern.domain.User;
import com.report.report_api_pattern.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<User> getUsers(){
        return repository.findAll();
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getSingleUser(@RequestParam String name) {
        List<User> users = repository.findByNameContainingIgnoreCase(name);
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }



    @PostMapping
    public ResponseEntity<User> postUser(@RequestBody User user){
        User savedUser = repository.save(user);
        return ResponseEntity.status(201).body(savedUser);
    }

}
