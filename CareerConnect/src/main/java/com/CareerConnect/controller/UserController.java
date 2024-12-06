package com.CareerConnect.controller;

import com.CareerConnect.entity.RecruiterProfile;
import com.CareerConnect.entity.User;
import com.CareerConnect.repo.RecruiterProfileRepo;
import com.CareerConnect.service.UserService;
import com.CareerConnect.service.UserTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.nio.file.Paths;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserTypeService userTypeService;
    private final UserService userService;
    private final RecruiterProfileRepo recruiterProfileRepo;

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
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }

    @GetMapping("/photos/recruiter/{userAccountId}/{username}")
    public ResponseEntity<Resource> getProfilePhoto(@PathVariable String username,
                                                    @PathVariable int userAccountId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            User user = userService.getUser(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            RecruiterProfile recruiterProfile = recruiterProfileRepo.findById(user.getUserId()).orElseThrow(() -> new UsernameNotFoundException("Profile not found"));

            if (userAccountId != recruiterProfile.getUserAccountId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } else {
                String UPLOAD_DIR = "photos/recruiter/" + userAccountId;
                java.nio.file.Path photoPath = Paths.get(UPLOAD_DIR, recruiterProfile.getProfilePhoto());
                Resource resource = new FileSystemResource(photoPath);

                if (resource.exists() && resource.isReadable()) {
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG)
                            .body(resource);
                } else {
                    return ResponseEntity.notFound().build();
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }




}
