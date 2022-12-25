package ru.alov.market.api.exception;

import java.util.List;

public class FieldValidationException extends RuntimeException {

  public FieldValidationException(List<String> errorFieldsMessages) {
    super(String.join(", ", errorFieldsMessages));
  }

  public FieldValidationException(String errorFieldMessages) {
    super(errorFieldMessages);
  }
}
