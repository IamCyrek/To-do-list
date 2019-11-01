package com.example.service;

import com.example.exception.ResourceNotFoundException;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(
                        String.format("User with email '%s' already exists!", email)
                )
        );
    }

    public List<User> getAllUsers(final Specification<User> request, final Sort sort) {
        return userRepository.findAll(request, sort);
    }

    public User createUser(final User user, PasswordEncoder encoder) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /*public void updateUserDTO(final UserDTO userDTO, PasswordEncoder encoder) {
        userRepository.findById(userDTO.getId())
                .map(oldUser -> {
                    if (!encoder.matches(userDTO.getPassword(), oldUser.getPassword()))
                        throw new IllegalArgumentException("Invalid password.");

                    userDTO.setPassword(oldUser.getPassword());
                    return userRepository.save(
                            userMapper.userDtoToUser(userDTO, oldUser.getCreatedAt()));
                }).orElseThrow(() ->
                        new ResourceNotFoundException("User with id = " + userDTO.getId() + " not found."));
    }

    public void updateUserPassword(final UserUpdatePasswordDTO dto, PasswordEncoder encoder) {
        User user = userRepository.findUserByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        if (!encoder.matches(dto.getOldPassword(), user.getPassword()))
            throw new IllegalArgumentException("Invalid email or password.");

        user.setPassword(encoder.encode(dto.getNewPassword()));

        userRepository.save(user);
    }*/

    public void deleteUser(final Long userId) {
        userRepository.deleteById(userRepository.findById(userId)
                .map(User::getId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + userId + " not found.")));
    }
}
