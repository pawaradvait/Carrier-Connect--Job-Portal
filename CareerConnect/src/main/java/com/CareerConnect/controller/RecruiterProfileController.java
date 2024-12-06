package com.CareerConnect.controller;

import com.CareerConnect.entity.RecruiterProfile;
import com.CareerConnect.entity.User;
import com.CareerConnect.service.RecruiterProfileService;
import com.CareerConnect.service.UserService;
import com.CareerConnect.util.FileHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
@RequiredArgsConstructor
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;
    private final UserService userService;

    @GetMapping("/")
    public String recruiterProfile(Model model) {
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
             Optional<User> user = userService.getUser(currentUsername);
             if(user.isPresent()){
                Optional<RecruiterProfile> recruiterProfile =  recruiterProfileService.getRecuriterProfilebyUser(user.get().getUserId());
                 if(recruiterProfile.isPresent()){
                     model.addAttribute("profile" , recruiterProfile.get());

                 }
             }
            return "recruiter_profile";

        }


        return "recruiter-profile.html";
    }

    @PostMapping("/addNew")
    public String addNewRecruiterProfile(RecruiterProfile recruiterProfile,
                                         @RequestParam("image") MultipartFile multipartFile
                                         ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            User users = userService.getUser(currentUsername).orElseThrow(() -> new UsernameNotFoundException("Could not " + "found user"));
            recruiterProfile.setUser(users);
            recruiterProfile.setUserAccountId(users.getUserId());
        }
        String filename=multipartFile.getOriginalFilename();
        recruiterProfile.setProfilePhoto(filename);
        RecruiterProfile savedUser = recruiterProfileService.saveRecruiterProfile(recruiterProfile);
        String uploadDir = "photos/recruiter/" + savedUser.getUserAccountId();

        try{
            FileHandler.saveFile(uploadDir,filename,multipartFile);
        }catch (IOException  e){
            System.out.println(e.getMessage());
        }

        return "redirect:/dashboard/";
    }

}
