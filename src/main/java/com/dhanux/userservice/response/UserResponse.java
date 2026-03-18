package com.dhanux.userservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author Dhanujaya(Dhanu)
 * @TimeStamp 19/03/2026 00:49
 * @ProjectDetails user-service
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponse <T> {
    private String message;
    private T user;
    private HttpStatus status;
}
