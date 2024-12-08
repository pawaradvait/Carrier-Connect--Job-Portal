package com.CareerConnect.controller;

import com.CareerConnect.entity.*;
import com.CareerConnect.repo.JobPostActivityRepo;
import com.CareerConnect.repo.JobSeekerProfileRepo;
import com.CareerConnect.service.JobPostActivityService;
import com.CareerConnect.service.JobSeekerApplyService;
import com.CareerConnect.service.UserService;
import com.CareerConnect.service.impl.JobPostActivityServiceImpl;
import com.CareerConnect.service.impl.JobSeekerSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class JobPostActivityController{

    private final UserService userService;
    private final JobPostActivityService jobPostActivityService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final JobSeekerSaveService jobSeekerSaveService;
    private final JobSeekerProfileRepo jobSeekerProfileRepo;


    @GetMapping("/dashboard/")
    public String dashboard(Model model,
                            @RequestParam(value = "job", required = false) String job,
                            @RequestParam(value = "location", required = false) String location,
                            @RequestParam(value = "partTime", required = false) String partTime,
                            @RequestParam(value = "fullTime", required = false) String fullTime,
                            @RequestParam(value = "freelance", required = false) String freelance,
                            @RequestParam(value = "remoteOnly", required = false) String remoteOnly,
                            @RequestParam(value = "officeOnly", required = false) String officeOnly,
                            @RequestParam(value = "partialRemote", required = false) String partialRemote,
                            @RequestParam(value = "today", required = false) boolean today,
                            @RequestParam(value = "days7", required = false) boolean days7,
                            @RequestParam(value = "days30", required = false) boolean days30

                            ){


        model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("fullTime", Objects.equals(partTime, "Full-Time"));
        model.addAttribute("freelance", Objects.equals(partTime, "Freelance"));

        model.addAttribute("remoteOnly", Objects.equals(partTime, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(partTime, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partTime, "Partial-Remote"));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job", job);
        model.addAttribute("location", location);


        LocalDate searchDate = null;
        List<JobPostActivity> jobPost = null;
        boolean dateSearchFlag = true;
        boolean remote = true;
        boolean type = true;

        if(days30){
            searchDate = LocalDate.now().minusDays(30);
        } else if (days7) {
            searchDate = LocalDate.now().minusDays(7);
        }else if (today) {
            searchDate = LocalDate.now();
        }else{
            dateSearchFlag =false;
        }

        if (partTime == null && fullTime == null && freelance == null) {
            partTime = "Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
            //this is to set for internship
            remote = false;
        }

        if (officeOnly == null && remoteOnly == null && partialRemote == null) {
            officeOnly = "Office-Only";
            remoteOnly = "Remote-Only";
            partialRemote = "Partial-Remote";
            type = false;
        }

        if (!dateSearchFlag && !remote && !type && !StringUtils.hasText(job) && !StringUtils.hasText(location)) {
            jobPost = jobPostActivityService.getAll();
        } else {
            jobPost = jobPostActivityService.search(job, location, Arrays.asList(partTime, fullTime, freelance),
                    Arrays.asList(remoteOnly, officeOnly, partialRemote), searchDate);
        }
          System.out.println(jobPost);
        Object currentUserProfile = userService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            model.addAttribute("username", currentUsername);
            if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("Recruiter"))) {

                User user = userService.getUser(currentUsername).orElseThrow(() -> new UsernameNotFoundException("user not found"));

                List<RecruiterJobDao> recruiterJobDaos = jobPostActivityService.getRecuriterJobs(user.getUserId());
                model.addAttribute("jobPost", recruiterJobDaos);
            } else {

                List<JobSeekerApply> jobSeekerApplies = jobSeekerApplyService.getJObByCandidate((JobSeekerProfile) currentUserProfile);
                List<JobSeekerSave> jobSeekerSaves = jobSeekerSaveService.getJobSeekerSave((JobSeekerProfile) currentUserProfile);
                boolean isActive = false;
                boolean isSaved = false;
                for (JobPostActivity jobPostActivity : jobPost) {

                    for (JobSeekerApply jobSeekerApply : jobSeekerApplies) {
                        if (jobSeekerApply.getJob().getJobPostId().equals(jobPostActivity.getJobPostId())) {
                            jobPostActivity.setIsActive(true);
                            isActive = true;
                            break;
                        }
                    }

                    for (JobSeekerSave jobSeekerSave : jobSeekerSaves) {
                        if (jobSeekerSave.getJob().getJobPostId().equals(jobPostActivity.getJobPostId())) {
                            jobPostActivity.setIsSaved(true);
                            isSaved = true;
                            break;
                        }
                    }

                    if (!isActive) {
                        jobPostActivity.setIsActive(false);
                    }
                    if (!isSaved) {
                        jobPostActivity.setIsSaved(false);


                    }

                    model.addAttribute("jobPost", jobPost);
                }
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


    @PostMapping("dashboard/edit/{id}")
    public String editJob(@PathVariable("id") int id, Model model) {

        JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
        model.addAttribute("jobPostActivity", jobPostActivity);
        model.addAttribute("user", userService.getCurrentUserProfile());
        return "add-jobs.html";
    }
}
