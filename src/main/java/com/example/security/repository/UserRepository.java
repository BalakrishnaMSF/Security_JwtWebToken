package com.example.security.repository;


import com.example.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM UserDetails u WHERE u.email = :identifier OR u.phNo = :identifier")
    User findByEmailOrPhNo(@Param("identifier") String identifier);
}


