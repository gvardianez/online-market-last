package ru.alov.market.api.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AnalyticsServiceAppError extends AppError {

  public enum AnalyticsServiceErrors {
    ANALYTICS_SERVICE_AUTH_INTEGRATION,
    ANALYTICS_SERVICE_CORE_INTEGRATION,
    ANALYTICS_SERVICE_INTERNAL_EXCEPTION,
    ANALYTICS_SERVICE_WEBCLIENT_REQUEST,
    ANALYTICS_SERVICE_BAD_REQUEST,
    ANALYTICS_SERVICE_FIELD_VALIDATION
  }

  public AnalyticsServiceAppError(String code, String message) {
    super(code, message);
  }
}
