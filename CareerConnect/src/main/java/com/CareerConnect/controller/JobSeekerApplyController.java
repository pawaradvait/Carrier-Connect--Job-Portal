package com.CareerConnect.controller;

import com.CareerConnect.entity.*;
import com.CareerConnect.repo.JobSeekerProfileRepo;
import com.CareerConnect.repo.RecruiterProfileRepo;
import com.CareerConnect.repo.UserRepo;
import com.CareerConnect.service.*;
import com.CareerConnect.service.impl.JobSeekerSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class JobSeekerApplyController {

    private final JobPostActivityService jobPostActivityService;
    private final UserService userService;
    private final JobSeekerApplyService jobSeekerApplyService;
    private final RecruiterProfileService recruiterProfileService;
    private final JobSeekerService jobSeekerService;
    private final JobSeekerSaveService jobSeekerSaveService;




    @GetMapping("job-details-apply/{id}")
    public String display(@PathVariable("id") int id, Model model) {
        JobPostActivity jobDetails = jobPostActivityService.getOne(id);
        List<JobSeekerApply> jobSeekerApplies = jobSeekerApplyService.getcandiadteforjobbasedonJObPost(jobDetails);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){

            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {


                    model.addAttribute("applyList", jobSeekerApplies);


            }else{


                boolean isActive=false;
                boolean issaved = false;
           JobSeekerProfile jobSeekerProfile = jobSeekerService.curentJObseeker();
List<JobSeekerSave> jobSeekerSaves = jobSeekerSaveService.getSavedJobs(jobSeekerProfile);
                for(JobSeekerApply jobSeekerApply : jobSeekerApplies) {
                    if (jobSeekerProfile.getUserId() == jobSeekerApply.getUserId().getUserId()) {
                       isActive=true;
                        jobDetails.setIsActive(true);
                        break;
                    }
                }

                for (JobSeekerSave jobSeekerSave : jobSeekerSaves) {
                    if (jobSeekerSave.getJob().getJobPostId() == jobDetails.getJobPostId()) {
                        issaved = true;
                        jobDetails.setIsSaved(true);
                        break;
                    }
                }




                JobSeekerApply jobSeekerApply = new JobSeekerApply();
                model.addAttribute("applyJob", jobSeekerApply);

                model.addAttribute("alreadyApplied", isActive);
                model.addAttribute("alreadySaved", issaved);

            }
        }




        model.addAttribute("jobDetails", jobDetails);
        model.addAttribute("user", userService.getCurrentUserProfile());
        return "job-details";
    }


    @PostMapping("job-details/apply/{id}")
    public String apply(@PathVariable("id") int id , JobSeekerApply jobSeekerApply){

        JobPostActivity jobDetails = jobPostActivityService.getOne(id);
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof  AnonymousAuthenticationToken)) {

            String email =authentication.getName();
            User user =userService.getUser(email).orElseThrow(() -> new UsernameNotFoundException("user not found"));

           Optional<JobSeekerProfile> jobSeekerProfile =jobSeekerService.getOne(user.getUserId());
            if(jobSeekerProfile.isPresent()) {

                jobSeekerApply.setUserId(jobSeekerProfile.get());
                jobSeekerApply.setJob(jobDetails);
                jobSeekerApply.setApplyDate(new Date());
                jobSeekerApplyService.saveJobSeekerApply(jobSeekerApply);
            }

        }
        return "redirect:/dashboard/";

    }
}
