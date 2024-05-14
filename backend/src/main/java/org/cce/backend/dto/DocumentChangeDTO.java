package org.cce.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentChangeDTO {
    private String id;
    private String left;
    private String right;
    private String content;
    private String operation;
    @JsonProperty("isdeleted")
    private boolean isdeleted;

    @JsonProperty("isbold")
    private boolean isbold;
    @JsonProperty("isitalic")
    private boolean isitalic;

    @JsonIgnore
    public boolean getIsDeleted(){
        return isdeleted;
    }
    @JsonIgnore
    public boolean getIsBold(){
        return isbold;
    }
    @JsonIgnore
    public boolean getIsItalic(){
        return isitalic;
    }

}


