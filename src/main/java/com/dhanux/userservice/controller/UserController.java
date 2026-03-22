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
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse<UserDto>> saveUser(@RequestBody @Valid UserDto userDto) {
        try {
            User registeredUser = userService.register(modelMapper.map(userDto, User.class));
            if (registeredUser != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(
                        new UserResponse<>("User saved successfully!",
                                modelMapper.map(registeredUser, UserDto.class),
                                HttpStatus.CREATED)
                );
            }
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new UserResponse<>(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR)
            );
        }
        return ResponseEntity.badRequest().body(new UserResponse<>("User not saved!", null, HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse<UserDto>> login(@RequestBody LoginDto loginDto) {
        User user = userService.login(modelMapper.map(loginDto, Login.class));
        if (user != null) {
            return ResponseEntity.ok(new UserResponse<>("Login successful!",
                    modelMapper.map(user, UserDto.class), HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new UserResponse<>("Invalid credentials!", null, HttpStatus.UNAUTHORIZED));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse<UserDto>> getUserById(@PathVariable int id) {
        User user = userService.getById(id);
        if (user != null) {
            return ResponseEntity.ok(new UserResponse<>("User found!",
                    modelMapper.map(user, UserDto.class), HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserResponse<>("User not found!", null, HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<UserResponse<List<UserDto>>> getAllUsers() {
        List<User> users = userService.getAll();
        // Case: Mapping List using Stream API
        List<UserDto> userDtos = users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();

        return ResponseEntity.ok(new UserResponse<>("Users retrieved successfully!",
                userDtos, HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse<UserDto>> updateUser(@PathVariable int id, @ModelAttribute UserDto userDto) {
        User updatedUser = userService.update(id, userDto);
        if (updatedUser != null) {
            return ResponseEntity.ok(new UserResponse<>("User updated successfully!",
                    modelMapper.map(updatedUser, UserDto.class), HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserResponse<>("Update failed: User not found!", null, HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse<Void>> deleteUser(@PathVariable int id) {
        if (userService.getById(id) != null) {
            userService.remove(id);
            return ResponseEntity.ok(new UserResponse<>("User deleted successfully!", null, HttpStatus.OK));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new UserResponse<>("Delete failed: User not found!", null, HttpStatus.NOT_FOUND));
    }


}