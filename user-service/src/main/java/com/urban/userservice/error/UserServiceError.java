package com.urban.userservice.error;

public class UserServiceError extends RuntimeException {

  private static final long serialVersionUID = 6357548932897618816L;
  private final int errorCode;

  public UserServiceError() {
    super();
    this.errorCode = DefaultErrorCodes.GENERIC_ERROR;
  }

  public UserServiceError(String message) {
    super(message);
    this.errorCode = DefaultErrorCodes.GENERIC_ERROR;
  }

  public UserServiceError(String message, Throwable cause) {
    super(message, cause);
    this.errorCode = DefaultErrorCodes.GENERIC_ERROR;
  }

  public UserServiceError(int errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public UserServiceError(int errorCode, String message, Throwable throwable) {
    super(message, throwable);
    this.errorCode = errorCode;
  }

  /**
   * Retrieves the error code.
   *
   * @return the error code of the error.
   */
  public int getErrorCode() {
    return errorCode;
  }
}