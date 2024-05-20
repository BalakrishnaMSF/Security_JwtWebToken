package com.example.security.model;


import com.example.security.constants.StringConstants;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = StringConstants.FIRSTNAME)
    private String firstName;

    @NotBlank(message = StringConstants.LASTNAME)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userInfo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    private List<PhoneNumber> phonenums = new ArrayList<>();

    @Email(message = StringConstants.INVALID_E)
    private String email;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

