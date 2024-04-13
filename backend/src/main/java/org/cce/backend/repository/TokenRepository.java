package org.cce.backend.repository;

import org.cce.backend.entity.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TokenRepository extends MongoRepository<Token,String> {
    List<Token> findByTokenKey();
}
