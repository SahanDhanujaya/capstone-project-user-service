package com.dhanux.userservice.dto;

import com.dhanux.userservice.validation.ValidImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Dhanujaya(Dhanu)
 * @TimeStamp 19/03/2026 00:47
 * @ProjectDetails user-service
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private String name;
    private String email;
    private String bio;
    private String phone;
    private String password;
    private String residency;
    private String role;
    private String picture;
}
