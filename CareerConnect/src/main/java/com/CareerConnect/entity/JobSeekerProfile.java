package com.CareerConnect.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "job_seeker_profile")
@NoArgsConstructor
@Getter
@Setter
public class JobSeekerProfile {
    @Id
    private int userAccountId;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private User userId;

    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String country;
    private String workAuthorization;
    private String employmentType;
    private String resume;

    @Column(nullable = true, length = 64)
    private String profilePhoto;

    @OneToMany(targetEntity = Skill.class, cascade = CascadeType.ALL, mappedBy = "jobSeekerProfile")
    private List<Skill>  skills;

    public JobSeekerProfile(User user) {

        this.userId = user;
    }
}
