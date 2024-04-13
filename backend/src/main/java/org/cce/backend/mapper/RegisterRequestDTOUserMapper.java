package org.cce.backend.mapper;

import org.cce.backend.dto.RegisterRequestDTO;
import org.cce.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface RegisterRequestDTOUserMapper {

    User RegisterRequestDTOToUser(RegisterRequestDTO registerRequestDTO);
}
