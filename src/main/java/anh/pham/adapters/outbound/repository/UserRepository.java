package anh.pham.adapters.outbound.repository;

import anh.pham.domain.model.User;
import anh.pham.exception.InvalidInformation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserRepository {
    private static final Set<User> USERS = new HashSet<>(Collections.singletonList(
            new User(1L, "NANA", 100_000)
    ));

    public User update(User user) {
        USERS.add(user);
        return user;
    }

    public User findById(Long id) {
        return USERS.stream().filter(u -> u.getId().equals(id)).findAny().orElseThrow(() ->  new InvalidInformation("Invalid user id !!!"));
    }
}
