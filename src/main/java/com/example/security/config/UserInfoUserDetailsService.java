package com.example.security.config;

import com.example.security.constants.StringConstants;
import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String emailOrPhNo) throws UsernameNotFoundException {
        User user = repository.findByEmailOrPhNo(emailOrPhNo);
        if (user != null) {
            return new UserDetailsInfo(user);
        } else {
            throw new UsernameNotFoundException(StringConstants.NO_USER_MAIL + emailOrPhNo);
        }
    }
}

