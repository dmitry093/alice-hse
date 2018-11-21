package ru.domru.tv.alicehse.services;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.domru.tv.alicehse.contracts.IUserService;
import ru.domru.tv.alicehse.models.User;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService implements IUserService {
    private Map<String, User> mapUsers;

    public UserService() {
        mapUsers = new HashMap<>();
    }

    @Override
    public Boolean userExists(String name) {
        return mapUsers.containsKey(name);
    }

    @Nullable
    @Override
    public User getUser(String name) {
        if (!userExists(name)) {
            return null;
        }
        return mapUsers.get(name);
    }

    @Override
    public void addUser(@NonNull User user) {
        mapUsers.put(user.getName(), user);
    }
}
