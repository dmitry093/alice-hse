package ru.hse.alice.controllers;

import org.springframework.web.bind.annotation.*;
import ru.hse.alice.contracts.IRequestProcessor;
import ru.hse.alice.contracts.IUserService;
import ru.hse.alice.models.dtos.User;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private IRequestProcessor processor;
    private IUserService userService;

    public AdminController(IRequestProcessor processor, IUserService userService) {
        if (processor == null) {
            throw new IllegalArgumentException("RequestProcessor is null");
        }
        if (userService == null) {
            throw new IllegalArgumentException("UserService is null");
        }
        this.processor = processor;
        this.userService = userService;
    }

    @PostMapping(value = "/user/")
    public User saveUser(@RequestBody User user) {
        userService.saveUser(user);

        return user;
    }

    @GetMapping(value = "/users")
    public List<User> getUsers() {
        return userService.getAll();
    }

    @GetMapping(value = "/sessions")
    public Map<String, User> getSessions() {
        return processor.getAllSessions();
    }
}
