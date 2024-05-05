package org.cce.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocTitleDTO {
    @NotNull(message = "Title is required")
    @JsonProperty(required = true)
    private String title;
}
