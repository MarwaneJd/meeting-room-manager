package com.mrm.meetingroommanager.controllers;

import com.mrm.meetingroommanager.entities.Role;
import com.mrm.meetingroommanager.entities.User;
import com.mrm.meetingroommanager.repositories.RoleRepository;
import com.mrm.meetingroommanager.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public AuthController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/login")
    public String loginPage() {
        // If user is already logged in, redirect to home page
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }

        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // If user is already logged in, redirect to home page
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        model.addAttribute("content", "register :: content");
        return "layout";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result, Model model) {
        // Check if username already exists
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            result.rejectValue("username", "error.user", "Username already exists");
        }

        if (result.hasErrors()) {
            model.addAttribute("content", "register :: content");
            return "layout";
        }

        // Set default role to USER if not specified
        if (user.getRole() == null) {
            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName("USER");
                        return roleRepository.save(newRole);
                    });
            user.setRole(userRole);
        }

        userService.saveUser(user);
        return "redirect:/login?registered";
    }
}
