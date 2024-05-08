package org.cce.backend.repository;

import org.cce.backend.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {
    List<Token> findByTokenKey(String token);
}
