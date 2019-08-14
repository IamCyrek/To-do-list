package com.example.model.mapper;

import com.example.model.User;
import com.example.model.dto.UserDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserDtoMapper {

    UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password")
    })
    @Named("userToUserDTO")
    UserDTO userToUserDTO(User user);

    @IterableMapping(qualifiedByName = "userToUserDTO")
    List<UserDTO> userToUserDTO(List<User> users);


    @Mappings({
            @Mapping(source = "userDTO.id", target = "id"),
            @Mapping(source = "userDTO.name", target = "name"),
            @Mapping(source = "userDTO.email", target = "email"),
            @Mapping(source = "userDTO.password", target = "password"),
            @Mapping(source = "createdAt", target = "createdAt")
    })
    User userDtoToUser(UserDTO userDTO, Date createdAt);
}
