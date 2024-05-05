package org.cce.backend.repository;

import org.cce.backend.entity.Doc;
import org.cce.backend.entity.User;
import org.cce.backend.entity.UserDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocRepository extends MongoRepository<Doc, String> {
    List<Doc> findByOwner(User owner);

    //UserDoc findByUsername(String username);
}
