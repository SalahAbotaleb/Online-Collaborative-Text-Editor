package org.cce.backend.entity;

import lombok.Data;
import org.cce.backend.enums.Permission;

@Data
public class UserDocSession {
    String DocId;
    Permission permission;
}
