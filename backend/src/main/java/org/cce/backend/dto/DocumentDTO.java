package org.cce.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDTO {
    String id;
    @JsonProperty(required = true)
    String title;
    @JsonProperty(required = true)
    String content;

}
