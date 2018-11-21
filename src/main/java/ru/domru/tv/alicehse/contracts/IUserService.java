package ru.domru.tv.alicehse.contracts;

import ru.domru.tv.alicehse.models.User;

public interface IUserService {
    Boolean userExists(String name);

    User getUser(String name);

    void addUser(User user);
}
