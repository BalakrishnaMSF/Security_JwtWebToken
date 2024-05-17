package com.example.FinalTask3.service;




import com.example.FinalTask3.constants.StringConstants;
import com.example.FinalTask3.model.UserDetails;
import com.example.FinalTask3.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDetails findByEmailOrPhNo(String emailOrPhNo) {
        logger.info(StringConstants.FIND_EM_PN, emailOrPhNo);
        return userRepository.findByEmailOrPhNo(emailOrPhNo);
    }

    public String addUserDetails(UserDetails userInfo) {
        logger.info("Adding user details: {}", userInfo);
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userRepository.save(userInfo);
        logger.info(StringConstants.USER_ADDED);
        return StringConstants.USER_ADD;
    }


    public List<UserDetails> getAllUsers() {
        logger.info("All users");
        return userRepository.findAll();
    }
}


