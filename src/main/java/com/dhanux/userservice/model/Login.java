package com.dhanux.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dhanujaya(Dhanu)
 * @TimeStamp 19/03/2026 21:17
 * @ProjectDetails user-service
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Login {
    private String email;
    private String password;
}
