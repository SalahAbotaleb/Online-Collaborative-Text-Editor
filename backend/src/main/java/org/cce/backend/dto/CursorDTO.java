package org.cce.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.module.FindException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CursorDTO {
    String username;
    int index;
    int length;
}
