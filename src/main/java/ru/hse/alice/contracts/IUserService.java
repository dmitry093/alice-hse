package ru.hse.alice.contracts;

import ru.hse.alice.models.dtos.User;

import java.util.List;

public interface IUserService {
    List<User> getAll();

    Boolean userExists(String firstName, String lastName);

    User getUser(String firstName, String lastName);

    void saveUser(User user);
}
