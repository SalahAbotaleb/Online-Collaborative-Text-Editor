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
public class RegisterRequestDTO {
    @JsonProperty(required = true)
    private String username;
    @JsonProperty(required = true)
    private String password;
    @JsonProperty(required = true)
    private String email;
}
