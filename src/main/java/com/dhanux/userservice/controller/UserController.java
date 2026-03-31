package com.dhanux.userservice.controller;

import com.dhanux.userservice.dto.LoginDto;
import com.dhanux.userservice.dto.UserDto;
import com.dhanux.userservice.exception.UserException;
import com.dhanux.userservice.model.Login;
import com.dhanux.userservice.model.User;
import com.dhanux.userservice.response.UserResponse;
import com.dhanux.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping(value = "/register")
    public ResponseEntity<UserResponse<UserDto>> saveUser(@RequestBody @Valid UserDto userDto) {
        try {
            User registeredUser = userService.register(modelMapper.map(userDto, User.class));
            if (registeredUser != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(
                        new UserResponse<UserDto>("User saved successfully!",
                                modelMapper.map(registeredUser, UserDto.class),
                                HttpStatus.CREATED)
                );
            }
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new UserResponse<UserDto>(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
        return ResponseEntity.badRequest().body(new UserResponse<>("User not saved!", null, HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse<UserDto>> login(@RequestBody LoginDto loginDto) {
        try {
            User user = userService.login(modelMapper.map(loginDto, Login.class));
            if (user != null) {
                return ResponseEntity.ok(new UserResponse<>("Login successful!",
                        modelMapper.map(user, UserDto.class), HttpStatus.OK));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new UserResponse<UserDto>("Invalid credentials!", null, HttpStatus.UNAUTHORIZED));
            }
        } catch (Exception e) {
            log.debug(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new UserResponse<UserDto>(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse<UserDto>> getUserById(@PathVariable String email) {
        User user = userService.getByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(new UserResponse<>("User found!",
                    modelMapper.map(user, UserDto.class), HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserResponse<UserDto>("User not found!", null, HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<UserResponse<List<UserDto>>> getAllUsers() {
        List<User> users = userService.getAll();
        // Case: Mapping List using Stream API
        List<UserDto> userDtos = users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();

        return ResponseEntity.ok(new UserResponse<List<UserDto>>("Users retrieved successfully!",
                userDtos, HttpStatus.OK));
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserResponse<UserDto>> updateUser(@PathVariable String email, @RequestBody UserDto userDto) {
        User updatedUser = userService.update(email, modelMapper.map(userDto, User.class));

        if (updatedUser != null) {
            return ResponseEntity.ok(new UserResponse<>("User updated successfully!",
                    modelMapper.map(updatedUser, UserDto.class), HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserResponse<UserDto>("Update failed: User not found!", null, HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<UserResponse<Void>> deleteUser(@PathVariable String email) {
        if (userService.getByEmail(email) != null) {
            userService.remove(email);
            return ResponseEntity.ok(new UserResponse<>("User deleted successfully!", null, HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserResponse<Void>("Delete failed: User not found!", null, HttpStatus.NOT_FOUND));
    }

    @PatchMapping(value ="/{email}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> uploadProfileImage(@PathVariable String email, @RequestParam("image") MultipartFile image) {
        try {
            // Get the bytes back from the service
            byte[] imageContent = userService.uploadImage(email, image);

            // Determine content type (defaulting to JPEG, or detect from file)
            MediaType contentType = MediaType.IMAGE_JPEG;
            if (image.getContentType() != null) {
                contentType = MediaType.parseMediaType(image.getContentType());
            }

            return ResponseEntity.ok()
                    .contentType(contentType)
                    .body(imageContent);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{email}/get-image")
    public ResponseEntity<byte[]> getUserImage(@PathVariable String email) {
        byte[] imageContent = userService.getImage(email);
        if (imageContent == null || imageContent.length == 0) {
            return ResponseEntity.notFound().build();
        }

        // 2. Ideally, retrieve the actual file extension/type from the database
        // For now, setting it dynamically is safer.
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Or logic to detect png/jpg
                .body(imageContent);
    }


}