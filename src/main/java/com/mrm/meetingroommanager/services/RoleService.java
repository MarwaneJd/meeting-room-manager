package com.mrm.meetingroommanager.services;

import com.mrm.meetingroommanager.entities.Role;
import com.mrm.meetingroommanager.entities.User;
import com.mrm.meetingroommanager.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    public List<User> getUsersByRole(Long roleId) {
        return roleRepository.findById(roleId)
                .map(Role::getUsers)
                .orElse(Collections.emptyList());
    }
}

