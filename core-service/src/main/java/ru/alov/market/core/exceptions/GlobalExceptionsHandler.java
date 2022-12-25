package ru.alov.market.core.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import ru.alov.market.api.exception.*;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandler {
  @ExceptionHandler
  public ResponseEntity<CoreServiceAppError> handleResourceNotFoundException(
      ResourceNotFoundException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CoreServiceAppError(
            CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_RESOURCE_NOT_FOUND.name(),
            e.getMessage()),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  public ResponseEntity<CoreServiceAppError> handleIllegalStateException(IllegalStateException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CoreServiceAppError(
            CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_BAD_REQUEST.name(), e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<CoreServiceAppError> handleFieldValidationException(
      FieldValidationException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CoreServiceAppError(
            CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_FIELD_VALIDATION.name(),
            e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<CoreServiceAppError> catchCartServiceIntegrationException(
      CartServiceIntegrationException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CoreServiceAppError(
            CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_CART_INTEGRATION.name(),
            e.getMessage()),
        HttpStatus.FAILED_DEPENDENCY);
  }

  @ExceptionHandler
  public ResponseEntity<CoreServiceAppError> catchPromotionServiceIntegrationException(
      PromotionServiceIntegrationException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CoreServiceAppError(
            CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_PROMOTION_INTEGRATION.name(),
            e.getMessage()),
        HttpStatus.FAILED_DEPENDENCY);
  }

  @ExceptionHandler
  public ResponseEntity<CoreServiceAppError> catchWebClientRequestException(
      WebClientRequestException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CoreServiceAppError(
            CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_WEBCLIENT_REQUEST.name(),
            e.getMessage()),
        HttpStatus.REQUEST_TIMEOUT);
  }

  @ExceptionHandler
  public ResponseEntity<CoreServiceAppError> handleConstraintValidationException(
      ConstraintViolationException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CoreServiceAppError(
            CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_FIELD_VALIDATION.name(),
            e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<CoreServiceAppError> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CoreServiceAppError(
            CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_FIELD_VALIDATION.name(),
            e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<CoreServiceAppError> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CoreServiceAppError(
            CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_BAD_REQUEST.name(), e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<CoreServiceAppError> handleMissingRequestValueException(
      MissingRequestValueException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CoreServiceAppError(
            CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_BAD_REQUEST.name(), e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<CoreServiceAppError> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CoreServiceAppError(
            CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_BAD_REQUEST.name(), e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<CoreServiceAppError> catchAnotherException(Exception e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CoreServiceAppError(
            CoreServiceAppError.CoreServiceErrors.CORE_SERVICE_INTERNAL_EXCEPTION.name(),
            HttpStatus.INTERNAL_SERVER_ERROR.name()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
