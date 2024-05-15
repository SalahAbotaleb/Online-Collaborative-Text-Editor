package org.cce.backend.mapper;

import org.cce.backend.dto.DocumentDTO;
import org.cce.backend.entity.Doc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.nio.charset.StandardCharsets;

@Mapper(componentModel = "spring", uses = UserDocMapper.class)
public interface DocumentMapper {
    @Mapping(source = "owner.username", target = "owner")
    //@Mapping(source = "content", target = "content", qualifiedByName = "bytesToString")
    @Mapping(target = "content", ignore = true)
    DocumentDTO toDto(Doc document);

//    default String map(byte[] value) {
//        return value != null ? new String(value, StandardCharsets.UTF_8) : null;
//    }
}
