package com.CareerConnect.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RecruiterProfile {

    @Id
    private int userAccountId;
    @OneToOne
    @JoinColumn(name = "user_account_id" )
    @MapsId
    private User user;

    private String firstName;
    private String lastName;
    private String city;

    private String state;

    private String country;

    private String company;


    @Column(nullable = true, length = 64)
    private String profilePhoto;



    public RecruiterProfile(User user) {
    this.user = user;
      }

    @Transient
    public String getPhotosImagePath() {
        if (profilePhoto == null) return null;
        return "/photos/recruiter/" + getUserAccountId() + "/" + profilePhoto;
    }


}
