package com.dhanux.userservice.exception;

import com.dhanux.userservice.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Dhanujaya(Dhanu)
 * @TimeStamp 19/03/2026 00:48
 * @ProjectDetails user-service
 */

@RestControllerAdvice
public class GlobalExceptionHandler extends Exception{
    @ExceptionHandler(UserException.class)
    public UserResponse<UserException> handleUserException (UserException ex) {
        return new UserResponse<>(ex.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public UserResponse<RuntimeException> handleRuntimeException (RuntimeException ex) {
        return new UserResponse<>(ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
