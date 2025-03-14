package com.management.demo.user;

import java.util.ArrayList;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

    public static User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        return new User(userDTO.getName(), userDTO.getEmail(), new ArrayList<>());
    }
}
