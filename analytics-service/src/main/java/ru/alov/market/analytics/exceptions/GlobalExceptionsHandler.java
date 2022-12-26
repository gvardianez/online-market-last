package ru.alov.market.analytics.exceptions;

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
import ru.alov.market.api.exception.AnalyticsServiceAppError;
import ru.alov.market.api.exception.CartServiceIntegrationException;
import ru.alov.market.api.exception.CoreServiceIntegrationException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandler {

  @ExceptionHandler
  public ResponseEntity<AnalyticsServiceAppError> catchAuthServiceIntegrationException(
      CartServiceIntegrationException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AnalyticsServiceAppError(
            AnalyticsServiceAppError.AnalyticsServiceErrors.ANALYTICS_SERVICE_AUTH_INTEGRATION
                .name(),
            e.getMessage()),
        HttpStatus.FAILED_DEPENDENCY);
  }

  @ExceptionHandler
  public ResponseEntity<AnalyticsServiceAppError> catchCoreServiceIntegrationException(
      CoreServiceIntegrationException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AnalyticsServiceAppError(
            AnalyticsServiceAppError.AnalyticsServiceErrors.ANALYTICS_SERVICE_CORE_INTEGRATION
                .name(),
            e.getMessage()),
        HttpStatus.FAILED_DEPENDENCY);
  }

  @ExceptionHandler
  public ResponseEntity<AnalyticsServiceAppError> catchWebClientRequestException(
      WebClientRequestException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AnalyticsServiceAppError(
            AnalyticsServiceAppError.AnalyticsServiceErrors.ANALYTICS_SERVICE_WEBCLIENT_REQUEST
                .name(),
            e.getMessage()),
        HttpStatus.REQUEST_TIMEOUT);
  }

  @ExceptionHandler
  public ResponseEntity<AnalyticsServiceAppError> handleConstraintValidationException(
      ConstraintViolationException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AnalyticsServiceAppError(
            AnalyticsServiceAppError.AnalyticsServiceErrors.ANALYTICS_SERVICE_FIELD_VALIDATION
                .name(),
            e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<AnalyticsServiceAppError> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AnalyticsServiceAppError(
            AnalyticsServiceAppError.AnalyticsServiceErrors.ANALYTICS_SERVICE_FIELD_VALIDATION
                .name(),
            e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<AnalyticsServiceAppError> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AnalyticsServiceAppError(
            AnalyticsServiceAppError.AnalyticsServiceErrors.ANALYTICS_SERVICE_BAD_REQUEST.name(),
            e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<AnalyticsServiceAppError> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AnalyticsServiceAppError(
            AnalyticsServiceAppError.AnalyticsServiceErrors.ANALYTICS_SERVICE_BAD_REQUEST.name(),
            e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<AnalyticsServiceAppError> handleMissingRequestValueException(
      MissingRequestValueException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AnalyticsServiceAppError(
            AnalyticsServiceAppError.AnalyticsServiceErrors.ANALYTICS_SERVICE_BAD_REQUEST.name(),
            e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<AnalyticsServiceAppError> catchAnotherException(Exception e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AnalyticsServiceAppError(
            AnalyticsServiceAppError.AnalyticsServiceErrors.ANALYTICS_SERVICE_INTERNAL_EXCEPTION
                .name(),
            HttpStatus.INTERNAL_SERVER_ERROR.name()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
