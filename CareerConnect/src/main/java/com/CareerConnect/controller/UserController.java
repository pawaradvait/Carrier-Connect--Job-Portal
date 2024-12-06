package com.CareerConnect.controller;

import com.CareerConnect.entity.User;
import com.CareerConnect.service.UserService;
import com.CareerConnect.service.UserTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserTypeService userTypeService;
    private final UserService userService;

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("getAllTypes" , userTypeService.getAllUserTypes());
        return "register.html";
    }

    @PostMapping("/register/new")
    public String registerUser(@Valid  @ModelAttribute("user") User user, Model model){
        Optional<User> user1 = userService.getUser(user.getEmail());

        if(user1.isPresent()){
            model.addAttribute("user", user);
            model.addAttribute("getAllTypes" , userTypeService.getAllUserTypes());
            model.addAttribute("error", "Email already exists");
 return "register.html";
        }
        userService.saveUser(user);
        return "redirect:/register";
    }

}
