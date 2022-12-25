package ru.alov.market.api.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PromotionServiceAppError extends AppError {

  public enum PromotionServiceErrors {
    PROMOTION_SERVICE_RESOURCE_NOT_FOUND,
    PROMOTION_SERVICE_INTERNAL_EXCEPTION,
    PROMOTION_SERVICE_BAD_REQUEST,
    PROMOTION_SERVICE_CORE_INTEGRATION,
    PROMOTION_SERVICE_FIELD_VALIDATION
  }

  public PromotionServiceAppError(String code, String message) {
    super(code, message);
  }
}
