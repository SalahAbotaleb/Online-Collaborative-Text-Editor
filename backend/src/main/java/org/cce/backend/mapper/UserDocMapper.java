package org.cce.backend.mapper;

import org.cce.backend.dto.UserDocDTO;
import org.cce.backend.entity.User;
import org.cce.backend.entity.UserDoc;
import org.cce.backend.repository.UserRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserDocMapper {
    public UserDocDTO toDto(UserDoc userDoc){
        return UserDocDTO.builder()
                .username(userDoc.getUser().getUsername())
                .permission(userDoc.getPermission()).build();
    }
}
