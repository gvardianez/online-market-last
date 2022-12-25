package ru.alov.market.api.exception;

public class MessagingServiceAppError extends AppError {

  public enum MessagingServiceErrors {
    MESSAGING_SERVICE_RESOURCE_NOT_FOUND,
    MESSAGING_SERVICE_INTERNAL_EXCEPTION,
    MESSAGING_SERVICE_BAD_REQUEST,
    MESSAGING_SERVICE_SECURITY,
    MESSAGING_SERVICE_FIELD_VALIDATION
  }

  public MessagingServiceAppError(String code, String message) {
    super(code, message);
  }
}
