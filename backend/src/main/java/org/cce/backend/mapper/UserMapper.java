package org.cce.backend.mapper;

import org.cce.backend.dto.UserDTO;
import org.cce.backend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
}
