package com.dhanux.userservice.model;

import com.dhanux.userservice.validation.ValidImage;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Dhanujaya(Dhanu)
 * @TimeStamp 19/03/2026 00:48
 * @ProjectDetails user-service
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @Email
    private String email;
    private String phone;
    private String bio;
    private String password;
    private String residency;
    private String role;
    private String picture;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
