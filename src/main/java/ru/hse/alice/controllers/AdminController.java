package ru.hse.alice.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.alice.contracts.IRequestProcessor;
import ru.hse.alice.contracts.IUserService;
import ru.hse.alice.models.User;

import java.util.List;
import java.util.Map;

@RestController
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

    @RequestMapping(value = "/users")
    public List<User> getUsers() {
        return userService.getAll();
    }

    @RequestMapping(value = "/sessions")
    public Map<String, User> getSessions() {
        return processor.getAllSessions();
    }
}
