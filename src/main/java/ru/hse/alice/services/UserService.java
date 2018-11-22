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
    public Boolean userExists(@Nullable String firstName, @Nullable String lastName) {
        if (firstName == null){
            firstName = "";
        }
        if (lastName == null){
            lastName = "";
        }
        return mapUsers.containsKey(firstName.toLowerCase() + lastName.toLowerCase());
    }

    @Nullable
    @Override
    public User getUser(@Nullable String firstName, @Nullable String lastName) {
        if (firstName == null){
            firstName = "";
        }
        if (lastName == null){
            lastName = "";
        }
        if (!userExists(firstName, lastName)) {
            return null;
        }
        return mapUsers.get(firstName.toLowerCase() + lastName.toLowerCase());
    }

    @Override
    public void addUser(@NonNull User user) {
        mapUsers.put(user.getFirstName().toLowerCase() + user.getLastName().toLowerCase(), user);
    }
}
