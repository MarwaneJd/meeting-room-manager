package com.mrm.meetingroommanager.dto.mapper;

import com.mrm.meetingroommanager.dto.RoleDTO;
import com.mrm.meetingroommanager.entities.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleDTO toDTO(Role role) {
        if (role == null) {
            return null;
        }

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());

        return roleDTO;
    }

    public Role toEntity(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }

        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());

        return role;
    }

    public void updateEntityFromDTO(RoleDTO roleDTO, Role role) {
        if (roleDTO == null || role == null) {
            return;
        }

        if (roleDTO.getName() != null) {
            role.setName(roleDTO.getName());
        }
    }
}
