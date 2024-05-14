package org.cce.backend.mapper;

import org.cce.backend.dto.DocumentChangeDTO;
import org.cce.backend.engine.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentChangeMapper {
    @Mapping(source = "right.id", target = "right")
    @Mapping(source = "left.id", target = "left")
    DocumentChangeDTO toDto(Item item);
}
