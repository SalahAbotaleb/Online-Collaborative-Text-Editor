package org.cce.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebSocketSession {
    private String username;
    private String docId;
}
