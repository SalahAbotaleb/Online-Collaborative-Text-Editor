package org.cce.backend.entity;

import jakarta.persistence.Embeddable;
import lombok.Builder;


@Embeddable
public class UserDocId implements java.io.Serializable {
    private String username;
    private Long docId;

}
