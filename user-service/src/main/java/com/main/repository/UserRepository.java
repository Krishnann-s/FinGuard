package com.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.main.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{ 
	boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
	Optional<User> findByUserName(String userName);
}
