package com.dhanux.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dhanujaya(Dhanu)
 * @TimeStamp 19/03/2026 00:47
 * @ProjectDetails user-service
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String residency;
    private String role;
    private String image;
}
