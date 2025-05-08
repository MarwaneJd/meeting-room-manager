package com.mrm.meetingroommanager.controllers;

import com.mrm.meetingroommanager.entities.Role;
import com.mrm.meetingroommanager.entities.User;
import com.mrm.meetingroommanager.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @GetMapping("/{id}/users")
    public List<User> getUsersByRole(@PathVariable Long id) {
        return roleService.getUsersByRole(id);
    }
}

