package com.management.demo.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        User user = UserMapper.toEntity(userDTO);
        user = userRepository.save(user);

        log.info("User created: {}", user.getId());

        return UserMapper.toDTO(user);
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
