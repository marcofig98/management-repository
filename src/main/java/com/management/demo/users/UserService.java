package com.management.demo.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserDTO createUser(UserDTO userDTO) {

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        User user = new User(userDTO.getName(), userDTO.getEmail(), new ArrayList<>());
        user = userRepository.save(user);

        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }


    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }


    public Optional<UserDTO> getUserById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(UserMapper::toDTO);
    }

    public UserDTO updateUser(UUID id, UserDTO userDTO) {


        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();


            if (userDTO.getName() != null && !userDTO.getName().trim().isEmpty() && !userDTO.getName().equals(userToUpdate.getName())) {
                userToUpdate.setName(userDTO.getName());
            }

            if (userDTO.getEmail() != null && !userDTO.getEmail().trim().isEmpty() && !userDTO.getEmail().equals(userToUpdate.getEmail())) {
                userToUpdate.setEmail(userDTO.getEmail());
            }

            userToUpdate = userRepository.save(userToUpdate);

            log.info("User updated: {}", userToUpdate.getId());

            return UserMapper.toDTO(userToUpdate);
        }

        throw new RuntimeException("User not found with id: " + id);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
        log.info("User deleted: {}", id);
    }
}
