package com.report.report_api_pattern.controller;

import com.report.report_api_pattern.domain.User;
import com.report.report_api_pattern.repository.UserRepository;
import com.report.report_api_pattern.service.impl.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository repository;


    private final UserService userService;

    public UserController(UserRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
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

    @PostMapping("/populate-users")
    public String populateUsers(@RequestParam(defaultValue = "5") int quantity) {
        userService.populateFakeUsers(quantity);
        return quantity + " users generated and saved.";
    }

}
