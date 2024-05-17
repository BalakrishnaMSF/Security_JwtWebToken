package com.example.FinalTask3.model;


import com.example.FinalTask3.constants.StringConstants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Entity
@Data
public class PhoneNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = StringConstants.REG_MB, message = StringConstants.INVALID_M)
    private String mbNo;

    private String type;
}

