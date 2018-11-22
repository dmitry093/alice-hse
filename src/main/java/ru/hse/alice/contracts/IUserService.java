package ru.hse.alice.contracts;

import ru.hse.alice.models.User;

public interface IUserService {
    Boolean userExists(String firstName, String lastName);

    User getUser(String firstName, String lastName);

    void saveUser(User user);
}
