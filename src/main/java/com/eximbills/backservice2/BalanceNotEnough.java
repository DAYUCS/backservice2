package com.eximbills.backservice2;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BalanceNotEnough extends RuntimeException {

    public BalanceNotEnough() { this("Balance is not enough!", null); }

    public BalanceNotEnough(String message) {
        this(message, null);
    }

    public BalanceNotEnough(String message, Throwable cause) {
        super(message, cause);
    }
}