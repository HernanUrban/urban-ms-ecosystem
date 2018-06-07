package com.urban.userservice.error;

public class UserValidationError extends UserServiceError {

    public UserValidationError(String message) {
        super(message);
    }

    public UserValidationError(int errorCode, String message) {
        super(errorCode, message);
    }

    public UserValidationError(int errorCode, String message, Throwable throwable){
        super(errorCode, message, throwable);
    }
}
