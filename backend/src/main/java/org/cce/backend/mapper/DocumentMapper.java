package org.cce.backend.mapper;

import org.cce.backend.dto.DocumentDTO;
import org.cce.backend.entity.Doc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserDocMapper.class)
public interface DocumentMapper {
    @Mapping(source = "owner.username", target = "owner")
    DocumentDTO toDto(Doc document);
}
