
package com.farmket.controller;

import com.farmket.model.User;
import com.farmket.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public String loginUser(
        @RequestParam("role") String role,
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        Model model,
        HttpSession session) {

        User user = userRepository
                .findByEmail(email.trim().toLowerCase())
                .orElse(null);

        if (user == null
                || !user.getPassword().equals(password)
                || !user.getRole().equals(role)) {

            model.addAttribute(
                    "error",
                    "Invalid email, password, or account type."
            );

            return "login";
        }
        session.setAttribute("loggedInEmail", user.getEmail());
        session.setAttribute("loggedInRole", user.getRole());
       if (user.getRole().equals("FARMER")) {
    return "redirect:/farmer-dashboard";
}

if (user.getRole().equals("BUYER")) {
    return "redirect:/buyer-dashboard";
}

        model.addAttribute("error", "This account type is not supported.");
        return "login";
    }
}
