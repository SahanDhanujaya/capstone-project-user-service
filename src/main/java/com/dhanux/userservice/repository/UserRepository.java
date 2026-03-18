package com.dhanux.userservice.repository;

import com.dhanux.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Dhanujaya(Dhanu)
 * @TimeStamp 19/03/2026 00:48
 * @ProjectDetails user-service
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> getUserByEmail(String email);
}
