package org.cce.backend.mapper;

import org.cce.backend.dto.DocumentDTO;
import org.cce.backend.entity.Doc;
import org.mapstruct.Mapper;
import org.springframework.data.mongodb.core.mapping.Document;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentDTO toDto(Doc document);
}
