package org.cce.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cce.backend.entity.User;
import org.cce.backend.enums.Permission;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDocDTO {
    @NotNull(message = "User is required")
    @JsonProperty(required = true)
    private String username;
    @NotNull(message = "Permission is required")
    @JsonProperty(required = true)
    private Permission permission;
}
