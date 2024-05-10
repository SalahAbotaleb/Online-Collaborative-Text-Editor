package org.cce.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentChangeDTO {
    String id;
    String left;
    String right;
    String content;
    boolean isdeleted;
    String operation;
}
