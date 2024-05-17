package com.example.FinalTask3.config;



import com.example.FinalTask3.constants.StringConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsInfo implements org.springframework.security.core.userdetails.UserDetails {

    private final String emailOrPhNo;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public UserDetailsInfo(com.example.FinalTask3.model.UserDetails userInfo) {
        emailOrPhNo = getEmailOrPhNo(userInfo);
        password = userInfo.getPassword();
        authorities = Arrays.stream(userInfo.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private String getEmailOrPhNo(com.example.FinalTask3.model.UserDetails userInfo) {
        if (isValidEmail(userInfo.getEmail())) {
            return userInfo.getEmail();
        } else if (isValidPhoneNumber(userInfo.getPhNo())) {
            return userInfo.getPhNo();
        } else {
            throw new IllegalArgumentException(StringConstants.INVALID);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = StringConstants.REG_MAIL;
        return email.matches(emailRegex);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = StringConstants.REG_MB;
        return phoneNumber.matches(phoneRegex);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return emailOrPhNo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

