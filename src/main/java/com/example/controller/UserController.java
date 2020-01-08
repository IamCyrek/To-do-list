package com.example.controller;

import com.example.controller.dto.UserDTO;
import com.example.controller.dto.UserFilterRequest;
import com.example.controller.mapper.UserMapper;
import com.example.model.specification.UserSpecification;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.configuration.Constants.API_USERS;
import static com.example.configuration.Constants.REGISTRATION;

@RestController
@RequestMapping(API_USERS)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final PasswordEncoder encoder;

    private final UserMapper userMapper;

    @GetMapping
    public List<UserDTO> getAllUsers(@Valid UserFilterRequest request, @SortDefault(sort = "id") Sort sort) {
        return userMapper.userToUserDTO((
                userService.getAllUsers(UserSpecification.getSpecification(request), sort)));
    }

    @PostMapping(value = REGISTRATION)
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody UserDTO userDTO) {
        userService.createUser(
                userMapper.userDtoToUser(userDTO, LocalDateTime.now()),
                encoder
        );
    }

    /*@PutMapping
    public Map<String, Boolean> updateUser(@Valid @RequestBody UserDTO userDTO) {
        userService.updateUserDTO(userDTO, encoder);

        return ControllerUtils.responseBuilder("updated", Boolean.TRUE);
    }

    @PutMapping("/updatePassword")
    public Map<String, Boolean> updateUserPassword(@Valid @RequestBody UserUpdatePasswordDTO dto) {
        userService.updateUserPassword(dto, encoder);

        return ControllerUtils.responseBuilder("updated", Boolean.TRUE);
    }*/

    @DeleteMapping
    public void deleteUser(@RequestParam(value = "id") Long userId) {
        userService.deleteUser(userId);
    }
}
