package ru.alov.market.api.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CartServiceAppError extends AppError {

  public enum CartServiceErrors {
    CART_SERVICE_RESOURCE_NOT_FOUND,
    CART_SERVICE_FIELD_VALIDATION,
    CART_SERVICE_CORE_INTEGRATION,
    CART_SERVICE_INTERNAL_EXCEPTION,
    CART_SERVICE_BAD_REQUEST,
    CART_SERVICE_WEBCLIENT_REQUEST
  }

  public CartServiceAppError(String code, String message) {
    super(code, message);
  }
}
