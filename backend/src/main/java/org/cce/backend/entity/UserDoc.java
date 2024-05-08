package org.cce.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cce.backend.enums.Permission;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserDoc {
    @Id
    @GeneratedValue
    private String id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Doc doc;

    private Permission permission;
}
