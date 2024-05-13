package org.cce.backend.mapper;

import org.cce.backend.dto.AuthenticationRequestDTO;
import org.cce.backend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticationDTOUserMapper {
    User AuthenticationRequestDTOToUser(AuthenticationRequestDTO registerRequestDTO);
}
