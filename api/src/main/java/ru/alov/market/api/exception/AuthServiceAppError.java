package ru.alov.market.api.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthServiceAppError extends AppError {

  public enum AuthServiceErrors {
    AUTH_SERVICE_RESOURCE_NOT_FOUND,
    AUTH_SERVICE_FIELD_VALIDATION,
    AUTH_SERVICE_INTERNAL_EXCEPTION,
    AUTH_SERVICE_BAD_REQUEST,
    AUTH_SERVICE_BAD_CREDENTIALS,
    AUTH_SERVICE_SECURITY
  }

  public AuthServiceAppError(String code, String message) {
    super(code, message);
  }
}
