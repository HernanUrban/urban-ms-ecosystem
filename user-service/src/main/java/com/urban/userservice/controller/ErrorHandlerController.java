package com.urban.userservice.controller;

import com.urban.userservice.error.DefaultErrorCodes;
import com.urban.userservice.error.ErrorDescription;
import javax.servlet.http.HttpServletRequest;

import com.urban.userservice.error.UserServiceError;
import com.urban.userservice.error.UserValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlerController {

  private final String moduleName = "user-service";

  /**
   * Handles invalid argument errors.
   *
   * @param request
   *          HTTP request.
   * @param exception
   *          the exception being handled.
   * @return an instance of ErrorDescription.
   */
  @ExceptionHandler({ UserValidationError.class, IllegalArgumentException.class, MethodArgumentNotValidException.class })
  public ResponseEntity<ErrorDescription> handleIllegalArgumentException(HttpServletRequest request,
                                                                         Exception exception) {
    ErrorDescription error =  ErrorDescription.buildError(DefaultErrorCodes.INVALID_ARGUMENTS,
        moduleName, exception, request);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({UserServiceError.class })
  public ResponseEntity<ErrorDescription> handleUserServiceError(HttpServletRequest request,
                                                                 UserServiceError exception) {
    ErrorDescription error =  ErrorDescription.buildError(exception.getErrorCode(),
            moduleName, exception, request);
    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
  }

  @ExceptionHandler({Exception.class })
  public ResponseEntity<ErrorDescription> handleException(HttpServletRequest request,
                                                                         Exception exception) {
    ErrorDescription error =  ErrorDescription.buildError(DefaultErrorCodes.GENERIC_ERROR,
        moduleName, exception, request);
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
