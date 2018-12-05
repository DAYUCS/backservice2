package com.eximbills.backservice2.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Balance implements Serializable {

    @Id
    private Long id;

    @Column(precision = 18, scale = 2)
    private float balance;

    @Column(length = 1)
    private String holdFlag;

    protected  Balance() {}

    public Balance(Long id, float balance, String holdFlag) {
        this.setId(id);
        this.setBalance(balance);
        this.setHoldFlag(holdFlag);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getHoldFlag() {
        return holdFlag;
    }

    public void setHoldFlag(String holdFlag) {
        this.holdFlag = holdFlag;
    }

    @Override
    public String toString() {
        return getId() + "," + getBalance() + "," + getHoldFlag();
    }
}
