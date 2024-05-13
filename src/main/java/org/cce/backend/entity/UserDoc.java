package org.cce.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cce.backend.enums.Permission;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserDoc {
    @EmbeddedId
    private UserDocId userDocId;

    @MapsId("username")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @MapsId("docId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docId", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Doc doc;

    private Permission permission;
}
