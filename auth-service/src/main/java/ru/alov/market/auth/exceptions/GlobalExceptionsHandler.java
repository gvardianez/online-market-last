package ru.alov.market.auth.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alov.market.api.exception.AuthServiceAppError;
import ru.alov.market.api.exception.FieldValidationException;
import ru.alov.market.api.exception.ResourceNotFoundException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandler {
  @ExceptionHandler
  public ResponseEntity<AuthServiceAppError> handleResourceNotFoundException(
      ResourceNotFoundException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AuthServiceAppError(
            AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_RESOURCE_NOT_FOUND.name(),
            e.getMessage()),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  public ResponseEntity<AuthServiceAppError> handleFieldValidationException(
      FieldValidationException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AuthServiceAppError(
            AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_FIELD_VALIDATION.name(),
            e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<AuthServiceAppError> handleIllegalStateException(IllegalStateException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AuthServiceAppError(
            AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_BAD_REQUEST.name(), e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<AuthServiceAppError> handleBadCredentialsException(
      BadCredentialsException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AuthServiceAppError(
            AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_BAD_CREDENTIALS.name(),
            e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<AuthServiceAppError> handleSecurityException(SecurityException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AuthServiceAppError(
            AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_SECURITY.name(), e.getMessage()),
        HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler
  public ResponseEntity<AuthServiceAppError> handleConstraintValidationException(
      ConstraintViolationException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AuthServiceAppError(
            AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_FIELD_VALIDATION.name(),
            e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<AuthServiceAppError> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AuthServiceAppError(
            AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_FIELD_VALIDATION.name(),
            e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<AuthServiceAppError> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AuthServiceAppError(
            AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_BAD_REQUEST.name(), e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<AuthServiceAppError> handleMissingRequestValueExceptionException(
      MissingRequestValueException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AuthServiceAppError(
            AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_BAD_REQUEST.name(), e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<AuthServiceAppError> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AuthServiceAppError(
            AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_BAD_REQUEST.name(), e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<AuthServiceAppError> catchAnotherException(Exception e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new AuthServiceAppError(
            AuthServiceAppError.AuthServiceErrors.AUTH_SERVICE_INTERNAL_EXCEPTION.name(),
            HttpStatus.INTERNAL_SERVER_ERROR.name()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
