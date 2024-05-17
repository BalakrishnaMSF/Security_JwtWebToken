package com.example.FinalTask3.repository;



import com.example.FinalTask3.model.Contact;
import com.example.FinalTask3.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByUserInfo(UserDetails userInfo);
    boolean existsByPhonenums_MbNo(String mbNo);
    List<Contact> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);

}

