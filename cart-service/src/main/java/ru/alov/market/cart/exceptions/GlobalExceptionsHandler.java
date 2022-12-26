package ru.alov.market.cart.exceptions;

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
import ru.alov.market.api.exception.CartServiceAppError;
import ru.alov.market.api.exception.CoreServiceIntegrationException;
import ru.alov.market.api.exception.FieldValidationException;
import ru.alov.market.api.exception.ResourceNotFoundException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandler {

  @ExceptionHandler
  public ResponseEntity<CartServiceAppError> handleFieldValidationException(
      FieldValidationException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CartServiceAppError(
            CartServiceAppError.CartServiceErrors.CART_SERVICE_FIELD_VALIDATION.name(),
            e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<CartServiceAppError> catchResourceNotFoundException(
      ResourceNotFoundException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CartServiceAppError(
            CartServiceAppError.CartServiceErrors.CART_SERVICE_RESOURCE_NOT_FOUND.name(),
            e.getMessage()),
        HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  public ResponseEntity<CartServiceAppError> catchWebClientRequestException(
      WebClientRequestException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CartServiceAppError(
            CartServiceAppError.CartServiceErrors.CART_SERVICE_WEBCLIENT_REQUEST.name(),
            e.getMessage()),
        HttpStatus.REQUEST_TIMEOUT);
  }

  @ExceptionHandler
  public ResponseEntity<CartServiceAppError> catchCoreServiceIntegrationException(
      CoreServiceIntegrationException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CartServiceAppError(
            CartServiceAppError.CartServiceErrors.CART_SERVICE_CORE_INTEGRATION.name(),
            e.getMessage()),
        HttpStatus.FAILED_DEPENDENCY);
  }

  @ExceptionHandler
  public ResponseEntity<CartServiceAppError> handleConstraintValidationException(
      ConstraintViolationException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CartServiceAppError(
            CartServiceAppError.CartServiceErrors.CART_SERVICE_FIELD_VALIDATION.name(),
            e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<CartServiceAppError> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CartServiceAppError(
            CartServiceAppError.CartServiceErrors.CART_SERVICE_FIELD_VALIDATION.name(),
            e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<CartServiceAppError> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CartServiceAppError(
            CartServiceAppError.CartServiceErrors.CART_SERVICE_BAD_REQUEST.name(), e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<CartServiceAppError> handleMissingRequestValueExceptionException(
      MissingRequestValueException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CartServiceAppError(
            CartServiceAppError.CartServiceErrors.CART_SERVICE_BAD_REQUEST.name(), e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<CartServiceAppError> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CartServiceAppError(
            CartServiceAppError.CartServiceErrors.CART_SERVICE_BAD_REQUEST.name(), e.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<CartServiceAppError> catchAnotherException(Exception e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(
        new CartServiceAppError(
            CartServiceAppError.CartServiceErrors.CART_SERVICE_INTERNAL_EXCEPTION.name(),
            HttpStatus.INTERNAL_SERVER_ERROR.name()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
