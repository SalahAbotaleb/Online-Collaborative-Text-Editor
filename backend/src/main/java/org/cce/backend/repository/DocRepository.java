package org.cce.backend.repository;

import org.cce.backend.entity.Doc;
import org.cce.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface DocRepository extends JpaRepository<Doc, Long> {

}
