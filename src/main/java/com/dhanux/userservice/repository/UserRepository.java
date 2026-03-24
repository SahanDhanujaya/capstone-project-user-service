package com.dhanux.userservice.repository;

import com.dhanux.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Dhanujaya(Dhanu)
 * @TimeStamp 19/03/2026 00:48
 * @ProjectDetails user-service
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    Optional<User> getUserByEmail(String email);
    void deleteByEmail(String email);

    boolean existsUserByEmail(String email);
}
