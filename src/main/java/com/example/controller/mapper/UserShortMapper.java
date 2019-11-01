package com.example.controller.mapper;

import com.example.model.User;
import com.example.controller.dto.UserShortDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel="spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserShortMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "createdAt", target = "createdAt")
    })
    @Named("userToUserShortDTO")
    UserShortDTO userToUserShortDTO(User user);

    @IterableMapping(qualifiedByName = "userToUserShortDTO")
    List<UserShortDTO> userToUserShortDTO(List<User> users);
}
