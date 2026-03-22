package com.dhanux.userservice.exception;

/**
 * @author Dhanujaya(Dhanu)
 * @TimeStamp 22/03/2026 23:37
 * @ProjectDetails user-service
 */

public class FileOperationException extends RuntimeException {

    public FileOperationException(String message) {
        super(message);
    }

    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}