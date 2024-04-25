package org.cce.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.cce.backend.enums.Permission;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@NoArgsConstructor
public class AccessDoc {
    @DBRef
    Doc doc;
    Permission permission;
}
