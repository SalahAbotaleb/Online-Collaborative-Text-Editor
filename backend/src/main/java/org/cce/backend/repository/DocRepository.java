package org.cce.backend.repository;

import org.cce.backend.entity.Doc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocRepository extends MongoRepository<Doc, String> {

}
