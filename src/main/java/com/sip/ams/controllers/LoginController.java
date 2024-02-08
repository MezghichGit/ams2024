package com.sip.ams.controllers;

import com.sip.ams.entities.User;
import com.sip.ams.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller

@RequestMapping("/login")
public class LoginController {

    public LoginController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	private final UserRepository userRepository;

    @GetMapping
    public String loginForm() {

        return "login";

    }
/*
    @PostMapping("")
    public String loginSubmit(Model model, @RequestParam String email, @RequestParam String password) {
        User user = userRepository.findByEmail(email);

        if (user!=null) {
                return "redirect:/home";
        }
        model.addAttribute("error", "Invalid email or password");
        return "login";
    }*/
    
    
}
