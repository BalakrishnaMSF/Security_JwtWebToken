package com.example.FinalTask3.repository;


import com.example.FinalTask3.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDetails, Long> {

    @Query("SELECT u FROM UserDetails u WHERE u.email = :identifier OR u.phNo = :identifier")
    UserDetails findByEmailOrPhNo(@Param("identifier") String identifier);
}


