package com.example.model.mapper;

import com.example.model.User;
import com.example.model.dto.UserShortDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserShortDtoMapper {

    UserShortDtoMapper INSTANCE = Mappers.getMapper(UserShortDtoMapper.class);

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
