package com.example.FinalTask3.controller;



import com.example.FinalTask3.constants.StringConstants;
import com.example.FinalTask3.model.UserDetails;
import com.example.FinalTask3.service.JwtService;
import com.example.FinalTask3.service.UserService;
import com.example.FinalTask3.token.AuthenticationRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/Users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserDetails userInfo) {
        log.info(StringConstants.REQ_REC_ADD_USER, userInfo);

        String result = userService.addUserDetails(userInfo);

        if (result != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(StringConstants.USER_ADDED);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringConstants.FAIL_USER_ADD);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateRequest(@RequestBody AuthenticationRequest authRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            log.info("User {} authenticated successfully", authRequest.getUserName());
            String token = jwtService.generateToken(authRequest.getUserName());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(StringConstants.AUTH_FAIL);
        }
    }

    @GetMapping("/all")
    public List<UserDetails> getAllUsers() {
        return userService.getAllUsers();
    }
}

