package com.devexperts.rest.validation;

import static java.lang.String.join;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.devexperts.exception.NegativeAmountException;
import com.devexperts.exception.ParameterNotPresentException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AccountControllerValidator {


  public void validateTransferParams(Long sourceId, Long targetId, Double amount) {
    List<String> nullParams = new ArrayList<>();

    if (isNull(sourceId)) {
      nullParams.add("sourceId");
    }
    if (isNull(targetId)) {
      nullParams.add("targetId");
    }
    if (isNull(amount)) {
      nullParams.add("amount");
    }
    if (nonNull(amount) && amount < 0) {
      throw new NegativeAmountException();
    }
    if (!nullParams.isEmpty()) {
      throw new ParameterNotPresentException(join(", ", nullParams));
    }
  }
}
