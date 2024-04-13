package org.cce.backend.repository;

import org.cce.backend.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository{
    HashMap<String, User> users = new HashMap<>();

    @Override
    public Optional<UserDetails> findByEmail(String email) {
        return Optional.ofNullable(users.get(email));
    }

    @Override
    public void addUser(User user) {
        users.put(user.getEmail(),user);
    }
}
