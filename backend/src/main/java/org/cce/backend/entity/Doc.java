package org.cce.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Doc {
    @Id
    @GeneratedValue
    private String id;
    @OneToOne
    private User owner;
    private String title;
    private String content;
    @OneToMany(mappedBy = "doc")
    private List<UserDoc> sharedWith;

}
