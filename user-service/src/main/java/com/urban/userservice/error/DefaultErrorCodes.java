package com.urban.userservice.error;

public interface DefaultErrorCodes {
  /**
   * Generic error code used when there is no defined error code.
   */
  public static final int GENERIC_ERROR = 1000;

  /**
   * A precondition failed validating function arguments.
   */
  public static final int INVALID_ARGUMENTS = 1050;

  /**
   * The entity was not found into any database.
   */
  public static final int ENTITY_NOT_FOUND = 2000;

  /**
   * User transaction error
   **/
  public static final int ACCOUNT_TRANSACTION_ERROR = 3000;

  /**
   * An action is not allowed to be executed.
   */
  public static final int NOT_ALLOWED_ERROR = 1100;

  /**
   * No transformer was registered for types.
   */
  public static final int NO_TRANSFORMER_REGISTERED_ERROR = 1010;

  /**
   * There is a constraint violation.
   */
  public static final int CONSTRAINT_VIOLATION = 2010;

}
