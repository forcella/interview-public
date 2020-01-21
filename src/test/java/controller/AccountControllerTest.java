package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.devexperts.ApplicationRunner;
import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.service.AccountService;
import java.net.URI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ApplicationRunner.class})
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
public class AccountControllerTest {

  @Autowired
  protected WebApplicationContext webAppContext;

  @Autowired
  @Qualifier("accountService")
  private AccountService service;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    mockMvc = webAppContextSetup(webAppContext).build();
  }

  @BeforeAll
  void addFakeData() {
    Account account1 = new Account(AccountKey.valueOf(1L), "Dennys", "Doe", 1412D);
    Account account2 = new Account(AccountKey.valueOf(2L), "Tinna", "Doe", 4600.60D);
    service.createAccount(account1);
    service.createAccount(account2);
  }

  @Test
  public void shouldExcept404StatusForNotFoundAccountSource() throws Exception {
    URI uri = UriComponentsBuilder
        .fromUriString("/api/operations/transfer?sourceId=55&targetId=56&amount=300").build()
        .encode()
        .toUri();
    mockMvc.perform(post(uri)).andExpect(status().isNotFound())
        .andExpect(content().string("Account with id: 55, was not found"));
  }

  @Test
  public void shouldExcept404StatusForNotFoundAccountTarget() throws Exception {
    URI uri = UriComponentsBuilder
        .fromUriString("/api/operations/transfer?sourceId=1&targetId=56&amount=300").build()
        .encode()
        .toUri();
    mockMvc.perform(post(uri)).andExpect(status().isNotFound())
        .andExpect(content().string("Account with id: 56, was not found"));
  }

  @Test
  public void shouldExcept400StatusWhenOneOrMoreParamsAreNotPresent() throws Exception {
    URI uri = UriComponentsBuilder
        .fromUriString("/api/operations/transfer").build()
        .encode()
        .toUri();
    mockMvc.perform(post(uri)).andExpect(status().isBadRequest())
        .andExpect(
            content().string("Parameter(s) 'sourceId, targetId, amount' not present on request"));
  }

  @Test
  public void shouldExcept500StatusWhenTheBalanceOfTheSourceIsInsufficient() throws Exception {
    URI uri = UriComponentsBuilder
        .fromUriString("/api/operations/transfer?sourceId=1&targetId=2&amount=99999").build()
        .encode()
        .toUri();
    mockMvc.perform(post(uri)).andExpect(status().isInternalServerError())
        .andExpect(
            content().string("Insufficient account balance"));
  }

  @Test
  public void shouldExcept400StatusWhenTheAmountIsAnNegativeValue() throws Exception {
    URI uri = UriComponentsBuilder
        .fromUriString("/api/operations/transfer?sourceId=1&targetId=2&amount=-300").build()
        .encode()
        .toUri();
    mockMvc.perform(post(uri)).andExpect(status().isBadRequest())
        .andExpect(
            content().string("The amount can't be an negative value"));
  }

  @Test
  public void shouldExcept20StatusForAnSuccessfulBalanceTransfer() throws Exception {
    URI uri = UriComponentsBuilder
        .fromUriString("/api/operations/transfer?sourceId=1&targetId=2&amount=300").build()
        .encode()
        .toUri();
    mockMvc.perform(post(uri)).andExpect(status().isOk())
        .andExpect(
            content().string("Successful transfer"));
  }

}
