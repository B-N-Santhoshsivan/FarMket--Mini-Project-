
package com.farmket.controller;

import com.farmket.model.User;
import com.farmket.repository.UserRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;

   public RegistrationController(UserRepository userRepository) {
    this.userRepository = userRepository;
}

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("fullname") String fullName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            @RequestParam("district") String district,
            Model model) {

        email = email.trim().toLowerCase();

        if (!role.equals("FARMER") && !role.equals("BUYER")) {
            model.addAttribute("error", "Please select a valid role.");
            return "register";
        }

        if (userRepository.existsByEmail(email)) {
            model.addAttribute(
                    "error",
                    "This email is already registered."
            );
            return "register";
        }

        User user = new User();

        user.setFullName(fullName.trim());
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setDistrict(district.trim());

        userRepository.save(user);

        model.addAttribute(
                "success",
                "Registration successful! You can now log in."
        );

        return "register";
    }
}

