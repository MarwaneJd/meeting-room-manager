package com.mrm.meetingroommanager.controllers;

import com.mrm.meetingroommanager.dto.RoleDTO;
import com.mrm.meetingroommanager.dto.UserDTO;
import com.mrm.meetingroommanager.dto.mapper.RoleMapper;
import com.mrm.meetingroommanager.dto.mapper.UserMapper;
import com.mrm.meetingroommanager.entities.Role;
import com.mrm.meetingroommanager.entities.User;
import com.mrm.meetingroommanager.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final UserMapper userMapper;

    public RoleController(RoleService roleService, RoleMapper roleMapper, UserMapper userMapper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles().stream()
                .map(roleMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        Role savedRole = roleService.createRole(role);
        return ResponseEntity.ok(roleMapper.toDTO(savedRole));
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable Long id) {
        List<User> users = roleService.getUsersByRole(id);
        List<UserDTO> userDTOs = users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }
}

