package com.CareerConnect.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "skills")
@Getter
@Setter
public class Skill {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String experienceLevel;
    private String yearsOfExperience;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_seeker_profile")
    private JobSeekerProfile jobSeekerProfile;
}
