package com.CareerConnect.config;

import com.CareerConnect.entity.User;
import com.CareerConnect.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(username);
  if(user.isPresent()){
      List<GrantedAuthority> grantedAuthorities= List.of(new SimpleGrantedAuthority(user.get().getUserTypeId().getUserTypeName()));
      return new org.springframework.security.core.userdetails.User(user.get().getEmail(),user.get().getPassword(),grantedAuthorities);
  }else{
      throw new UsernameNotFoundException("User not found");
  }
    }
}
