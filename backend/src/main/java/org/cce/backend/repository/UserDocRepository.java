package org.cce.backend.repository;

import jakarta.transaction.Transactional;
import org.cce.backend.entity.Doc;
import org.cce.backend.entity.UserDoc;
import org.cce.backend.entity.UserDocId;
import org.cce.backend.enums.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDocRepository extends JpaRepository<UserDoc, UserDocId> {
    @Query("select ud.doc from UserDoc ud where ud.user.username = ?1")
    List<Doc> getDocsByUser_Username(String username);

    @Modifying
    @Transactional
    @Query("delete from UserDoc ud where ud.userDocId.username = ?1 and ud.userDocId.docId = ?2 and (select d.owner.username from Doc d where d.id = ?2) = ?3")
    int deleteUserDocBy(String username, Long docId, String owner);

    @Modifying
    @Transactional
    @Query("update UserDoc ud set ud.permission = ?4 where ud.userDocId.username = ?1 and ud.userDocId.docId = ?2 and (select d.owner.username from Doc d where d.id = ?2) = ?3")
    int updateUserDocBy(String username, Long docId, String owner, Permission permission);

}
