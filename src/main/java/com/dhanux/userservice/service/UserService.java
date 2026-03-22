package com.dhanux.userservice.service;

import com.dhanux.userservice.dto.UserDto;
import com.dhanux.userservice.model.Login;
import com.dhanux.userservice.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Dhanujaya(Dhanu)
 * @TimeStamp 19/03/2026 00:49
 * @ProjectDetails user-service
 */

public interface UserService {
    User register (User user);
    User login (Login login);
    User update (int id, UserDto user);
    List<User> getAll ();
    void remove (int id);
    User getById (int id);
}
