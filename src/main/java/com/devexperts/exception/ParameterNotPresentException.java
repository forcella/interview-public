package com.devexperts.exception;

import static java.lang.String.format;

public class ParameterNotPresentException extends RuntimeException {

  public ParameterNotPresentException(String parameter) {
    super(format("Parameter(s) '%s' not present on request", parameter));
  }

}
