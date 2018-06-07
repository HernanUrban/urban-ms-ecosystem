package com.urban.userservice.error;

public class UserNotFoundError extends UserServiceError {

    public UserNotFoundError(String message) {
        super(message);
    }

    public UserNotFoundError(int errorCode, String message) {
        super(errorCode, message);
    }

    public UserNotFoundError(int errorCode, String message, Throwable throwable){
        super(errorCode, message, throwable);
    }
}
