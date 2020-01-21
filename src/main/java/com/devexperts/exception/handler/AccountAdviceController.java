package com.devexperts.exception.handler;

import com.devexperts.exception.AccountNotFoundException;
import com.devexperts.exception.InsufficientBalanceException;
import com.devexperts.exception.NegativeAmountException;
import com.devexperts.exception.ParameterNotPresentException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccountAdviceController {


  @ExceptionHandler(value = {ParameterNotPresentException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleParameterNotPresent(
      ParameterNotPresentException e) {
    return e.getMessage();
  }

  @ExceptionHandler(value = {NegativeAmountException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleNegativeAmount(
      NegativeAmountException e) {
    return e.getMessage();
  }

  @ExceptionHandler(value = {AccountNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleAccountNotFound(
      AccountNotFoundException e) {
    return e.getMessage();
  }

  @ExceptionHandler(value = {InsufficientBalanceException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String handleInsufficientBalance(
      InsufficientBalanceException e) {
    return e.getMessage();
  }
}
