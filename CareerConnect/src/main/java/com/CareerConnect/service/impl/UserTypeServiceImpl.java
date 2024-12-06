package com.CareerConnect.service.impl;

import com.CareerConnect.entity.UserType;
import com.CareerConnect.repo.UserTypeRepo;
import com.CareerConnect.service.UserTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTypeServiceImpl implements UserTypeService {

    private final UserTypeRepo userTypeRepo;

    @Override
    public List<UserType> getAllUserTypes() {
        return userTypeRepo.findAll();

    }
}
