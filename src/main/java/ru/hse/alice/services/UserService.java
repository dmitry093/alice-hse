package ru.hse.alice.services;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.hse.alice.contracts.IUserService;
import ru.hse.alice.models.User;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService implements IUserService {
    private Map<String, User> mapUsers;

    public UserService() {
        mapUsers = new HashMap<>();
        mapUsers.put("дмитрийлатышев", new User("Дмитрий", "Латышев", null, true));
    }

    @Override
    public Boolean userExists(String firstName, String lastName) {
        return mapUsers.containsKey(firstName + lastName.toLowerCase());
    }

    @Nullable
    @Override
    public User getUser(String firstName, String lastName) {
        if (!userExists(firstName, lastName)) {
            return null;
        }
        return mapUsers.get(firstName + lastName.toLowerCase());
    }

    @Override
    public void addUser(@NonNull User user) {
        mapUsers.put(user.getFirstName().toLowerCase() + user.getLastName().toLowerCase(), user);
    }
}
