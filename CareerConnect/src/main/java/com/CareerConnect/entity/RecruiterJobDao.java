package com.CareerConnect.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterJobDao {
    private Long totalCandidates;
    private Integer jobPostId;
    private String jobTitle;
    private JobLocation jobLocationId;
    private JobCompany jobCompanyId;

}
