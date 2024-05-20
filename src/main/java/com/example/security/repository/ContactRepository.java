package com.example.security.repository;


import com.example.security.model.Contact;
import com.example.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByUserInfo(User userInfo);
    boolean existsByPhonenums_MbNo(String mbNo);
    List<Contact> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);

}

