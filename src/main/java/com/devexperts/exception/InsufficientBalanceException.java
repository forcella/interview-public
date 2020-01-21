package com.devexperts.exception;

public class InsufficientBalanceException extends RuntimeException{
  public InsufficientBalanceException(){
    super("Insufficient account balance");
  }
}
