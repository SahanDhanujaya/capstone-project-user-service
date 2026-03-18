package com.dhanux.userservice.service;

import com.dhanux.userservice.model.User;
import com.dhanux.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Dhanujaya(Dhanu)
 * @TimeStamp 19/03/2026 00:49
 * @ProjectDetails user-service
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public User register(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User login(User user) {
        List<User> userByEmail = userRepository.getUserByEmail(user.getEmail());
        User existingUser = userByEmail.getFirst();
        if (user.getPassword().equals(existingUser.getPassword())) {
            return existingUser;
        }
        return null;
    }

    @Override
    public User update(int id, User user) {
        if (userRepository.existsById(id)) {
            user.setUpdatedAt(LocalDateTime.now());
            user.setId(id);
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void remove(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public User getById(int id) {
        if (userRepository.existsById(id)) {
            Optional<User> byId = userRepository.findById(id);
            if (byId.isPresent()) {
                return byId.get();
            }
        }
        return null;
    }
}
