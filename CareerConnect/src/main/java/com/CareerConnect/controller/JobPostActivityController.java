package com.CareerConnect.controller;

import com.CareerConnect.entity.JobPostActivity;
import com.CareerConnect.entity.RecruiterJobDao;
import com.CareerConnect.entity.User;
import com.CareerConnect.repo.JobPostActivityRepo;
import com.CareerConnect.service.JobPostActivityService;
import com.CareerConnect.service.UserService;
import com.CareerConnect.service.impl.JobPostActivityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class JobPostActivityController{

    private final UserService userService;
    private final JobPostActivityService jobPostActivityService;

    @GetMapping("/dashboard/")
    public String dashboard(Model model){

        Object currentUserProfile = userService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            model.addAttribute("username", currentUsername);
            if(authentication.getAuthorities().stream().anyMatch(r->r.getAuthority().equals("Recruiter"))){

  User user = userService.getUser(currentUsername).orElseThrow(()-> new UsernameNotFoundException("user not found"));

                List<RecruiterJobDao> recruiterJobDaos = jobPostActivityService.getRecuriterJobs(user.getUserId());
                model.addAttribute("jobPost", recruiterJobDaos);
            }
        }

        model.addAttribute("user", currentUserProfile);
        return "dashboard.html";

    }


    @GetMapping("/dashboard/add")
    public String addJobs(Model model) {
        model.addAttribute("jobPostActivity", new JobPostActivity());
        model.addAttribute("user", userService.getCurrentUserProfile());
        return "add-jobs.html";
    }

    @PostMapping("/dashboard/addNew")
    public String saveJob(@ModelAttribute("jobPostActivity") JobPostActivity jobPostActivity) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            User user =userService.getUser(currentUsername).orElseThrow(()-> new UsernameNotFoundException("user not found"));
            jobPostActivity.setPostedById(user);
            jobPostActivity.setPostedDate(new java.util.Date());
            jobPostActivityService.addNew(jobPostActivity);

        }

        return "redirect:/dashboard/";
    }


    @GetMapping("dashboard/edit/{id}")
    public String editJob(@PathVariable("id") int id, Model model) {

        JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
        model.addAttribute("jobPostActivity", jobPostActivity);
        model.addAttribute("user", userService.getCurrentUserProfile());
        return "add-jobs";
    }
}
