package com.example.security.token;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String userName;
    private String password;
}

