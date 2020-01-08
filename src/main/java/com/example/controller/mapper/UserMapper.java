package com.example.controller.mapper;

import com.example.model.User;
import com.example.controller.dto.UserDTO;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel="spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

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
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(target = "taskSet", ignore = true),
            @Mapping(target = "roleSet", ignore = true)
    })
    User userDtoToUser(UserDTO userDTO, LocalDateTime createdAt);
}
