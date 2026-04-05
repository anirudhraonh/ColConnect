package com.example.ColConnect.controllers;

import com.example.ColConnect.models.User;
import com.example.ColConnect.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    UserRepository userRepository;

    @ModelAttribute("currentUser")
    public User addCurrentUser(Principal principal){
        if (principal == null) return  null;
        return userRepository.findByName(principal.getName());
    }
}
