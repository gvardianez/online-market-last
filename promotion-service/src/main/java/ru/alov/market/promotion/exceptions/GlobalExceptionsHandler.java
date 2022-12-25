package ru.alov.market.promotion.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alov.market.api.exception.*;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandler {

    @ExceptionHandler
    public ResponseEntity<PromotionServiceAppError> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new PromotionServiceAppError(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_RESOURCE_NOT_FOUND.name(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PromotionServiceAppError> catchCoreServiceIntegrationException(CoreServiceIntegrationException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new PromotionServiceAppError(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_CORE_INTEGRATION.name(), e.getMessage()), HttpStatus.FAILED_DEPENDENCY);
    }

    @ExceptionHandler
    public ResponseEntity<PromotionServiceAppError> handleConstraintValidationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new PromotionServiceAppError(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_FIELD_VALIDATION.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<PromotionServiceAppError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new PromotionServiceAppError(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_FIELD_VALIDATION.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<PromotionServiceAppError> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new PromotionServiceAppError(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_BAD_REQUEST.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<PromotionServiceAppError> handleMissingRequestValueException(MissingRequestValueException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new PromotionServiceAppError(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_BAD_REQUEST.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<PromotionServiceAppError> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new PromotionServiceAppError(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_BAD_REQUEST.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<PromotionServiceAppError> catchAnotherException(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new PromotionServiceAppError(PromotionServiceAppError.PromotionServiceErrors.PROMOTION_SERVICE_INTERNAL_EXCEPTION.name(), HttpStatus.INTERNAL_SERVER_ERROR.name()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
