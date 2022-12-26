package ru.alov.market.api.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CoreServiceAppError extends AppError {

  public enum CoreServiceErrors {
    CORE_SERVICE_RESOURCE_NOT_FOUND,
    CORE_SERVICE_CART_INTEGRATION,
    CORE_SERVICE_PROMOTION_INTEGRATION,
    CORE_SERVICE_INTERNAL_EXCEPTION,
    CORE_SERVICE_BAD_REQUEST,
    CORE_SERVICE_FIELD_VALIDATION,
    CORE_SERVICE_WEBCLIENT_REQUEST
  }

  public CoreServiceAppError(String code, String message) {
    super(code, message);
  }
}
