package org.cce.backend.repository;

import org.cce.backend.entity.Doc;
import org.cce.backend.entity.Token;
import org.cce.backend.entity.UserDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDocRepository extends JpaRepository<UserDoc,Long> {
    @Query("select ud.doc from UserDoc ud where ud.user.username = ?1")
    List<Doc> getDocsByUser_Username(String username);
}
