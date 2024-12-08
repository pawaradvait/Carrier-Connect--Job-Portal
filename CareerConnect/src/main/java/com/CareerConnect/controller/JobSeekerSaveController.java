package com.CareerConnect.controller;

import com.CareerConnect.entity.JobPostActivity;
import com.CareerConnect.entity.JobSeekerProfile;
import com.CareerConnect.entity.JobSeekerSave;
import com.CareerConnect.entity.User;
import com.CareerConnect.service.JobPostActivityService;
import com.CareerConnect.service.JobSeekerService;
import com.CareerConnect.service.UserService;
import com.CareerConnect.service.impl.JobSeekerSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class JobSeekerSaveController {

    @Autowired
    private UserService userService;
    @Autowired
    private JobSeekerSaveService jobSeekerSaveService;
    @Autowired
    private JobPostActivityService jobPostActivityService;
    @Autowired
    private JobSeekerService jobSeekerService;


    @PostMapping("job-details/save/{id}")
    public String jobSave(@PathVariable("id") int id, JobSeekerSave jobSeekerSave) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            String email = authentication.getName();
            User user = userService.getUser(email).orElseThrow(() -> new UsernameNotFoundException("user not found"));

            Optional<JobSeekerProfile> jobSeekerProfile = jobSeekerService.getOne(user.getUserId());
            if (jobSeekerProfile.isPresent()) {

                jobSeekerSave.setUserId(jobSeekerProfile.get());
                jobSeekerSave.setJob(jobPostActivityService.getOne(id));
                jobSeekerSaveService.saveJobSeekerSave(jobSeekerSave);
            }


        }
        return "redirect:/dashboard/";

    }

    @GetMapping("saved-jobs/")
    public String savedJobs(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            String email = authentication.getName();
            User user = userService.getUser(email).orElseThrow(() -> new UsernameNotFoundException("user not found"));

            List<JobPostActivity> jobPost = new ArrayList<>();

            Optional<JobSeekerProfile> jobSeekerProfile = jobSeekerService.getOne(user.getUserId());


            if (jobSeekerProfile.isPresent()) {

                List<JobSeekerSave> jobSeekerSaves = jobSeekerSaveService.getSavedJobs(jobSeekerProfile.get());

                for (JobSeekerSave jobSeekerSave : jobSeekerSaves) {

                    jobPost.add(jobSeekerSave.getJob());
                }

            }
            model.addAttribute("jobPost", jobPost);
            model.addAttribute("user", jobSeekerProfile.get());



        }
        return "saved-jobs";
    }


}
