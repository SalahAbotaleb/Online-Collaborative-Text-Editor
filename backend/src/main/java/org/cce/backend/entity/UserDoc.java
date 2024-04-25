package org.cce.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.cce.backend.enums.Permission;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@NoArgsConstructor
public class UserDoc {
    @DBRef
    private User user;
    private Permission permission;
}
