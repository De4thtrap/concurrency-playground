package ru.astondevs.meetup.concurrency.guava.striped;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
class UserAccount {
    private final String userId;

    private BigDecimal balance;

    public UserAccount(String userId) {
        this.userId = userId;
        this.balance = BigDecimal.ZERO;
    }

    public BigDecimal changeBalance(BigDecimal diff) {
        this.balance = balance.add(diff);
        return this.balance;
    }

}
