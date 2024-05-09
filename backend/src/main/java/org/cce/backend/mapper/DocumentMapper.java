package org.cce.backend.mapper;

import org.cce.backend.dto.DocumentDTO;
import org.cce.backend.entity.Doc;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {UserDocMapper.class})
public interface DocumentMapper {
    DocumentDTO toDto(Doc document);
}
