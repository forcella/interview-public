package com.devexperts.rest;

import com.devexperts.account.Account;
import com.devexperts.rest.validation.AccountControllerValidator;
import com.devexperts.service.AccountService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operations")
public class AccountController extends AbstractAccountController {

  private AccountService service;
  private AccountControllerValidator validator;

  public AccountController(
      @Qualifier("accountService") AccountService service,
      AccountControllerValidator validator) {
    this.service = service;
    this.validator = validator;
  }

  @PostMapping("/transfer")
  public ResponseEntity<String> transfer(Long sourceId, Long targetId, Double amount) {
    validator.validateTransferParams(sourceId, targetId, amount);

    service.transfer(new Account(sourceId), new Account(targetId), amount);
    return ResponseEntity.ok("Successful transfer");
  }
}
