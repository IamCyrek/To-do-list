package com.example.service;

import com.example.exception.ResourceNotFoundException;
import com.example.model.User;
import com.example.model.dto.UserUpdatePasswordDTO;
import com.example.model.dto.UserShortDTO;
import com.example.model.dto.UserDTO;
import com.example.model.mapper.UserShortDtoMapper;
import com.example.model.mapper.UserDtoMapper;
import com.example.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static String md5Apache(String st) {
        return DigestUtils.md5Hex(st);
    }

    public List<UserShortDTO> getAllUsers() {
        return UserShortDtoMapper.INSTANCE.userToUserShortDTO(userRepository.findAllByOrderByIdAsc());
    }

    public User createUser(final User user) {
        userRepository.findUserByEmail(user.getEmail())
                .ifPresent(smth -> {
                    throw new IllegalArgumentException("User with email '" + smth.getEmail() + "' already exists!");
                });

        user.setPassword(md5Apache(user.getPassword()));
        return userRepository.save(user);
    }

    public void updateUserDTO(final UserDTO userDTO) {
        userRepository.findUserByEmailAndIdIsNot(userDTO.getEmail(), userDTO.getId())
                .ifPresent(smth -> {
                    throw new IllegalArgumentException("User with email '" + smth.getEmail() + "' already exists!");
                });

        userRepository.findById(userDTO.getId())
                .map(oldUser -> {
                    if (!oldUser.getPassword().equals(md5Apache(userDTO.getPassword())))
                        throw new IllegalArgumentException("Invalid password.");

                    userDTO.setPassword(oldUser.getPassword());
                    return userRepository.save(
                            UserDtoMapper.INSTANCE.userDtoToUser(userDTO, oldUser.getCreatedAt()));
                }).orElseThrow(() ->
                        new ResourceNotFoundException("User with id = " + userDTO.getId() + " not found."));
    }

    public void updateUserPassword(final UserUpdatePasswordDTO dto) {
        User user = userRepository.findUserByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        if (!user.getPassword().equals(md5Apache(dto.getOldPassword())))
            throw new IllegalArgumentException("Invalid email or password.");

        user.setPassword(md5Apache(dto.getNewPassword()));

        userRepository.save(user);
    }

    public void deleteUser(final Long userId) {
        userRepository.deleteById(userRepository.findById(userId)
                .map(User::getId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + userId + " not found.")));
    }

}
