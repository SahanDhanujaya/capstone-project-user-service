package com.dhanux.userservice.service;

import com.dhanux.userservice.dto.UserDto;
import com.dhanux.userservice.exception.FileOperationException;
import com.dhanux.userservice.model.Login;
import com.dhanux.userservice.model.User;
import com.dhanux.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Dhanujaya(Dhanu)
 * @TimeStamp 19/03/2026 00:49
 * @ProjectDetails user-service
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Value("${app.storage.path:uploads/profiles}")
    private String storagePathStr;

    private Path storagePath;

    @Override
    public User register(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User login(Login login) {
        List<User> userByEmail = userRepository.getUserByEmail(login.getEmail());
        User existingUser = userByEmail.getFirst();
        if (login.getPassword().equals(existingUser.getPassword())) {
            return existingUser;
        }
        return null;
    }

    @Override
    public User update(int id, UserDto userDto) {
        return userRepository.findById(id).map(existingUser -> {
            // Map DTO to the existing entity to preserve fields not in DTO
            modelMapper.map(userDto, existingUser);

            if (userDto.getImage() != null && !userDto.getImage().isEmpty()) {
                String pictureId = UUID.randomUUID().toString();
                savePicture(pictureId, userDto.getImage());
                existingUser.setPicture(pictureId);
            }

            existingUser.setUpdatedAt(LocalDateTime.now());
            existingUser.setId(id); // Ensure ID remains the same
            return userRepository.save(existingUser);
        }).orElse(null);
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

    private Path storagePath() {
        if (storagePath == null) {
            storagePath = Paths.get(storagePathStr);
        }
        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new FileOperationException(
                    "Failed to create storage directory: " + storagePath.toAbsolutePath(), e);
        }
        return storagePath;
    }

    private void savePicture(String pictureId, MultipartFile file) {
        try {
            Path root = storagePath(); // Ensures directory exists
            Files.copy(file.getInputStream(), root.resolve(pictureId));
            log.debug("Picture saved successfully: {}", pictureId);
        } catch (IOException e) {
            throw new FileOperationException("Could not store the file. Error: " + e.getMessage());
        }
    }
}
