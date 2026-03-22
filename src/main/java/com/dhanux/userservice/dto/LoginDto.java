package com.dhanux.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dhanujaya(Dhanu)
 * @TimeStamp 19/03/2026 21:15
 * @ProjectDetails user-service
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDto {
    private String email;
    private String password;
}
