package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.exception.AccountNotFoundException;
import com.devexperts.exception.InsufficientBalanceException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

  private final List<Account> accounts = new ArrayList<>();

  @Override
  public void clear() {
    accounts.clear();
  }

  @Override
  public void createAccount(Account account) {
    try {
      getAccount(account.getAccountKey().getAccountId());
      throw new IllegalArgumentException();
    } catch (AccountNotFoundException e) {
      accounts.add(account);
    }
  }

  @Override
  public Account getAccount(long id) {
    //There is the possibility to change the accounts variable type to HashMap, the performance is
    //way faster than ArrayList but occupies more space in memory.
    return accounts.stream()
        .filter(account -> account.getAccountKey().equals(AccountKey.valueOf(id)))
        .findAny()
        .orElseThrow(() -> new AccountNotFoundException(id));

  }

  @Override
  public void transfer(Account source, Account target, double amount) {
    asyncTransfer(source, target, amount);
  }


  public int getTotalOfAccounts() {
    return accounts.size();
  }

  private void asyncTransfer(Account source, Account target, double amount) {
    Account finalSource = getAccount(source.getAccountKey().getAccountId());
    Account finalTarget = getAccount(target.getAccountKey().getAccountId());

    if (finalSource.getBalance() >= amount) {
      CompletableFuture.runAsync(() -> {
        finalSource.setBalance(finalSource.getBalance() - amount);
        finalTarget.setBalance(finalTarget.getBalance() + amount);
      });
    } else {
      throw new InsufficientBalanceException();
    }
  }

}
