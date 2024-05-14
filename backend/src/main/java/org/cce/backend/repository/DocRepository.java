package org.cce.backend.repository;

import org.cce.backend.entity.Doc;
import org.cce.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocRepository extends JpaRepository<Doc, Long> {
    List<Doc> findByOwner(User owner);

    Optional<Doc> findByIdAndOwner(Long id, User owner);

    List<Doc> findByOwner_Username(String username);

    Optional<Doc> findByIdAndOwner_Username(Long id, String username);

    @Query("select d from Doc d where d.owner.username = ?1 or d in (select ud.doc from UserDoc ud where ud.user.username = ?1)")
    List<Doc> findByUsername(String username);

    Optional<Doc> getDocById(Long id);

}
