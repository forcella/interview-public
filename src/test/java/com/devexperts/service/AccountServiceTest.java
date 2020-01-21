package com.devexperts.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountServiceTest {

  private AccountService service;


  @BeforeEach
  public void setup() {
    service = new AccountServiceImpl();
    Account account1 = new Account(AccountKey.valueOf(1L), "Maria", "Doe", 1000D);
    Account account4 = new Account(AccountKey.valueOf(4L), "Mike", "Second", 1100D);
    service.createAccount(account1);
    service.createAccount(account4);
  }

  @Test
  public void shouldCreateNewAccount() {
    Account account = new Account(AccountKey.valueOf(2L), "John", "Doe", 1000D);
    int totalOfAccountsBeforeInsert = service.getTotalOfAccounts();
    int expectBeforeInsert = 2;
    service.createAccount(account);
    int totalOfAccountsAfterInsert = service.getTotalOfAccounts();
    int expectAfterInsert = 3;

    assertEquals(expectBeforeInsert, totalOfAccountsBeforeInsert);
    assertEquals(expectAfterInsert, totalOfAccountsAfterInsert);
  }

  @Test
  public void shouldThrowErrorWhenAccountIsAlreadyCreated() {
    Account account = new Account(AccountKey.valueOf(1L), "Error", "Doe", 1000D);
    assertThrows(IllegalArgumentException.class, () -> {
      service.createAccount(account);
    });
  }

  @Test
  public void shouldFindAnExistingAccount() {
    Account account = service.getAccount(1L);

    assertNotNull(account);
    assertThat(account, allOf(
        hasProperty("firstName", equalTo("Maria")),
        hasProperty("lastName", equalTo("Doe")),
        hasProperty("balance", equalTo(1000D)),
        hasProperty("accountKey",
            hasProperty("accountId", equalTo(1L)))
    ));
  }

  @Test
  public void shouldGetNullForNonExistingAccount() {
    Account account = service.getAccount(3L);
    assertNull(account);
  }

  @Test
  public void shouldClearAccounts() {
    boolean shouldHaveMoreThanZeroAccountsBeforeClear = service.getTotalOfAccounts() > 0;
    int valueExpectedAfterClearAccounts = 0;

    assertTrue(shouldHaveMoreThanZeroAccountsBeforeClear);
    service.clear();
    assertEquals(valueExpectedAfterClearAccounts, service.getTotalOfAccounts());
  }

  @Test
  public void shouldTransferBalanceFromSourceAccountToTarget() {
    Account target = service.getAccount(1L);
    Account source = service.getAccount(4L);
    double amount = 299.51D;
    double targetBalance = target.getBalance() + amount;
    double sourceBalance = source.getBalance() - amount;

    service.transfer(source, target, amount);

    assertEquals(target.getBalance(), targetBalance);
    assertEquals(source.getBalance(), sourceBalance);
  }
}
