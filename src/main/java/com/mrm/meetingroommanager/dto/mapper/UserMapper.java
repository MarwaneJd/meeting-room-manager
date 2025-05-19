package com.mrm.meetingroommanager.dto.mapper;

import com.mrm.meetingroommanager.dto.UserCreateDTO;
import com.mrm.meetingroommanager.dto.UserDTO;
import com.mrm.meetingroommanager.entities.Role;
import com.mrm.meetingroommanager.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());

        if (user.getRole() != null) {
            userDTO.setRoleId(user.getRole().getId());
            userDTO.setRoleName(user.getRole().getName());
        }

        return userDTO;
    }

    public User toEntity(UserCreateDTO userCreateDTO) {
        if (userCreateDTO == null) {
            return null;
        }

        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(userCreateDTO.getPassword()); // Note: This should be encoded before saving

        if (userCreateDTO.getRoleId() != null) {
            Role role = new Role();
            role.setId(userCreateDTO.getRoleId());
            user.setRole(role);
        }

        return user;
    }

    public void updateEntityFromDTO(UserCreateDTO userCreateDTO, User user) {
        if (userCreateDTO == null || user == null) {
            return;
        }

        if (userCreateDTO.getUsername() != null) {
            user.setUsername(userCreateDTO.getUsername());
        }

        if (userCreateDTO.getEmail() != null) {
            user.setEmail(userCreateDTO.getEmail());
        }

        if (userCreateDTO.getPassword() != null) {
            user.setPassword(userCreateDTO.getPassword()); // Note: This should be encoded before saving
        }

        if (userCreateDTO.getRoleId() != null) {
            Role role = new Role();
            role.setId(userCreateDTO.getRoleId());
            user.setRole(role);
        }
    }
}
