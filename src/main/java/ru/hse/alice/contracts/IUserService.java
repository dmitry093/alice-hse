package ru.hse.alice.contracts;

import ru.hse.alice.models.User;

public interface IUserService {
    Boolean userExists(String name);

    User getUser(String name);

    void addUser(User user);
}
