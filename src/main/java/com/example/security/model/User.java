package com.example.security.model;

import com.example.security.constants.StringConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;



@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @NotBlank(message = StringConstants.INVALID_USER)
    private String userName;

    @Column(unique = true)
    @Email(message = StringConstants.INVALID_E)
    private String email;

    @Column(unique = true)
    @Pattern(regexp = "[789]\\d{9}", message = StringConstants.INVALID_M)
    private String phNo;

    private String password;
    private String role;

}

