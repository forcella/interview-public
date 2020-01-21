package com.devexperts.exception;

import static java.lang.String.format;

public class AccountNotFoundException extends RuntimeException {

  public AccountNotFoundException(long accountId) {
    super(format("Account with id: %s, was not found", accountId));
  }
}
