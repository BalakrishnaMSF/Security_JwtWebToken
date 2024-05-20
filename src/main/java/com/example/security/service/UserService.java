package com.example.security.service;


import com.example.security.constants.StringConstants;
import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByEmailOrPhNo(String emailOrPhNo) {
        logger.info(StringConstants.FIND_EM_PN, emailOrPhNo);
        return userRepository.findByEmailOrPhNo(emailOrPhNo);
    }

    public String addUserDetails(User userInfo) {
        logger.info("Adding user details: {}", userInfo);
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userRepository.save(userInfo);
        logger.info(StringConstants.USER_ADDED);
        return StringConstants.USER_ADD;
    }


    public List<User> getAllUsers() {
        logger.info("All users");
        return userRepository.findAll();
    }
}


