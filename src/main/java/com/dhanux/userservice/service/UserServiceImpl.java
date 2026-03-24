package com.dhanux.userservice.service;

import com.dhanux.userservice.dto.UserDto;
import com.dhanux.userservice.exception.FileOperationException;
import com.dhanux.userservice.model.Login;
import com.dhanux.userservice.model.User;
import com.dhanux.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
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
@Transactional
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
        Optional<User> userByEmail = userRepository.getUserByEmail(login.getEmail());
        if (userByEmail.isPresent()) {
            User existingUser = userByEmail.get();
            if (login.getPassword().equals(existingUser.getPassword())) {
                return existingUser;
            }
        }
        return null;
    }

    @Override
    public User update(String email, User user) {
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null){
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setBio(user.getBio());
            existingUser.setUpdatedAt(LocalDateTime.now());
            existingUser.setCreatedAt(existingUser.getCreatedAt());
            return userRepository.save(existingUser);
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void remove(String email) {
        if (userRepository.existsUserByEmail(email)) {
            userRepository.deleteByEmail(email);
        }
    }

    @Override
    public User getByEmail(String email) {
        if (userRepository.existsUserByEmail(email)) {
            Optional<User> byId = userRepository.getUserByEmail(email);
            if (byId.isPresent()) {
                return byId.get();
            }
        }
        return null;
    }

    @Override
    public byte[] uploadImage(String email, MultipartFile image) throws IOException {
        // 1. Create directory if not exists
        Path root = Paths.get(storagePathStr);
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        // 2. Save file with unique name
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path targetPath = root.resolve(fileName);
        Files.copy(image.getInputStream(), targetPath);

        // 3. Update User Record
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPicture(fileName);
            userRepository.save(user);
        }

        // 4. Return the bytes so the controller can send them to the UI
        return image.getBytes();
    }

    @Override
    public byte[] getImage(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPicture() != null) {
            Path imagePath = Paths.get(storagePathStr).resolve(user.getPicture());
            if (Files.exists(imagePath)) {
                try {
                    return Files.readAllBytes(imagePath);
                } catch (IOException e) {
                    log.error("Error reading image file: {}", e.getMessage());
                    throw new FileOperationException("Failed to read profile picture.");
                }
            }
        }
        return null;
    }


}
