package org.cce.backend.mapper;

import org.cce.backend.dto.UserDocDTO;
import org.cce.backend.entity.User;
import org.cce.backend.entity.UserDoc;
import org.cce.backend.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface UserDocMapper {
    @Mapping(source = "user.username", target = "username")
    UserDocDTO userDocToUserDocDTO(UserDoc userDoc);
}
