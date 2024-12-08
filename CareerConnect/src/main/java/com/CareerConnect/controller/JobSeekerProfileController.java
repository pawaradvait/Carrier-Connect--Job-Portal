package com.CareerConnect.controller;

import com.CareerConnect.entity.JobSeekerProfile;
import com.CareerConnect.entity.Skill;
import com.CareerConnect.entity.User;
import com.CareerConnect.service.JobSeekerService;
import com.CareerConnect.service.UserService;
import com.CareerConnect.util.FileDownload;
import com.CareerConnect.util.FileHandler;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.boot.Banner;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/job-seeker-profile")
@RequiredArgsConstructor
public class JobSeekerProfileController {

    private final JobSeekerService jobSeekerService;
    private final UserService userService;

    @GetMapping("/")
    public String jobSeekerProfile(Model model
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            String currentUsername = authentication.getName();
            User user = userService.getUser(currentUsername).orElseThrow(() -> new UsernameNotFoundException("user not found"));
            Optional<JobSeekerProfile> jobSeekerProfile = jobSeekerService.getOne(user.getUserId());
            List<Skill> skills = new ArrayList<>();
            if (jobSeekerProfile.isPresent()) {

                if (jobSeekerProfile.get().getSkills().isEmpty()) {
                    skills.add(new Skill());
                }
                if(jobSeekerProfile.get().getSkills().size()>0){
                    skills.addAll(jobSeekerProfile.get().getSkills());
                }
                jobSeekerProfile.get().setSkills(skills);
                model.addAttribute("skills", skills);
                model.addAttribute("profile", jobSeekerProfile.get());
            }
        }
        return "job-seeker-profile";
    }


    @PostMapping("/addNew")
    public String addNew(@ModelAttribute("profile") JobSeekerProfile jobSeekerProfile,
                         @RequestParam("image") MultipartFile multipartFile,
                         @RequestParam("pdf") MultipartFile pdf,
                         Model model

                         ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            User user = userService.getUser(currentUsername).orElseThrow(() -> new UsernameNotFoundException("user not found"));
            jobSeekerProfile.setUserId(user);
            jobSeekerProfile.setUserAccountId(user.getUserId());
            String filename = multipartFile.getOriginalFilename();
            jobSeekerProfile.setProfilePhoto(filename);
            String resume = pdf.getOriginalFilename();
            jobSeekerProfile.setResume(resume);
    System.out.println(jobSeekerProfile.toString());
  List<Skill> skillss =new ArrayList<>();
            for(Skill skill : jobSeekerProfile.getSkills()) {
                skill.setJobSeekerProfile(jobSeekerProfile);

                skillss.add(skill);
                System.out.println(skill.getId()+""+skill.getName() + "" + skill.getExperienceLevel() + "" + skill.getYearsOfExperience());
            }
jobSeekerProfile.setSkills(skillss);
            model.addAttribute("profile", jobSeekerProfile);
            model.addAttribute("skills",
            jobSeekerProfile.getSkills());
            jobSeekerService.saveJobSeekerProfile(jobSeekerProfile);
            String uploadDir = "photos/candidate/" + jobSeekerProfile.getUserAccountId();
            try {
                FileHandler.saveFile(uploadDir, filename, multipartFile);
                FileHandler.saveFile(uploadDir, resume, pdf);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            try {
                FileHandler.saveFile(uploadDir, resume, pdf);
            } catch (IOException e) {

            }
        }
        return "redirect:/dashboard/";
    }


    @GetMapping("/{id}")
    public String candidateProfile(@PathVariable("id") int id,Model model){
        Optional<JobSeekerProfile> jobSeekerProfile = jobSeekerService.getOne(id);
        if(jobSeekerProfile.isPresent()){
            model.addAttribute("profile" ,jobSeekerProfile.get());
        }
        return "job-seeker-profile.html";
    }

    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(
            @RequestParam("userID") int userID,
            @RequestParam("fileName") String fileName
    )   {

        FileDownload fileDownload = new FileDownload();
        Resource resource = null;
try {

     resource =fileDownload.getFile("photos/candidate/" + userID, fileName);


}catch (IOException e) {
    System.out.println(e.getMessage());
}
 if(resource != null){

     String contextType ="application/pdf";
     String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
     return ResponseEntity.ok().contentType(MediaType.parseMediaType(contextType)).header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);

 }else{
     return  new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
 }


    }

}