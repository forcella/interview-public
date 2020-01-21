package com.devexperts.exception;

public class NegativeAmountException extends RuntimeException {

  public NegativeAmountException() {
    super("The amount can't be an negative value");
  }
}
