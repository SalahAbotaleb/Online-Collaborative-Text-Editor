package org.cce.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
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
public class AccessDoc {
    @Id

    String id;
    String userId;
    Permission permission;
}
