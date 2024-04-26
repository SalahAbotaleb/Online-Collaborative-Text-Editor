package org.cce.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cce.backend.entity.User;
import org.cce.backend.entity.UserDoc;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDTO {
    private String id;
    private User owner;
    @JsonProperty(required = true)
    private String title;
//    @JsonProperty(required = true)
    private String content;

    private List<UserDoc> sharedWith;

}
