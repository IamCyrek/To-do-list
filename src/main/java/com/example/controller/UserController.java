package com.example.controller;

import com.example.model.User;
import com.example.model.dto.UserDTO;
import com.example.model.dto.UserShortDTO;
import com.example.model.dto.UserUpdatePasswordDTO;
import com.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private Map<String, Boolean> responseBuilder(final String s, final Boolean b) {
        Map<String, Boolean> response = new HashMap<>();
        response.put(s, b);
        return response;
    }

    @GetMapping
    public List<UserShortDTO> getAllUserDTOs() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public Map<String, Boolean> updateUserDTO(@Valid @RequestBody UserDTO userDTO) {
        userService.updateUserDTO(userDTO);

        return responseBuilder("updated", Boolean.TRUE);
    }

    @PutMapping("/updatePassword")
    public Map<String, Boolean> updateUserPassword(@Valid @RequestBody UserUpdatePasswordDTO dto) {
        userService.updateUserPassword(dto);

        return responseBuilder("updated", Boolean.TRUE);
    }

    @DeleteMapping
    public Map<String, Boolean> deleteUser(@RequestParam(value = "id") Long userId) {
        userService.deleteUser(userId);

        return responseBuilder("deleted", Boolean.TRUE);
    }
}
