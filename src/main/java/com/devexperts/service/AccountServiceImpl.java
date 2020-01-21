package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    long accountId = account.getAccountKey().getAccountId();
    if (Objects.nonNull(getAccount(accountId))) {
      throw new IllegalArgumentException();
    } else {
      accounts.add(account);
    }
  }

  @Override
  public Account getAccount(long id) {
    //There is the possibility to change the accounts variable type to HashMap, the performance is
    //way faster than ArrayList but occupies more space in memory.
    return accounts.stream().parallel()
        .filter(account -> account.getAccountKey().equals(AccountKey.valueOf(id)))
        .findAny()
        .orElse(null);

  }

  @Override
  public void transfer(Account source, Account target, double amount) {
    asyncTransfer(source, target, amount);
  }


  public int getTotalOfAccounts() {
    return accounts.size();
  }

  private void asyncTransfer(Account source, Account target, double amount) {
    CompletableFuture<Account> futureSource = CompletableFuture
        .supplyAsync(() -> getAccount(source.getAccountKey().getAccountId()));
    CompletableFuture<Account> futureTarget = CompletableFuture
        .supplyAsync(() -> getAccount(target.getAccountKey().getAccountId()));

    CompletableFuture.allOf(futureSource, futureTarget).thenApplyAsync(aVoid -> {
      try {
        if (futureSource.get().getBalance() >= amount) {
          futureSource.get().setBalance(futureSource.get().getBalance() - amount);
          futureTarget.get().setBalance(futureTarget.get().getBalance() + amount);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return aVoid;
    });
  }
}
