package org.cce.backend.repository;

import org.cce.backend.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<UserDetails> findByUsername(String username);
}
