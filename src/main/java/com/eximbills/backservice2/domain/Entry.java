package com.eximbills.backservice2.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Entry implements Serializable {

    @Id
    @Column(length = 36)
    private String id;

    @Column(precision = 18, scale = 2)
    private float entryAmount;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "balance_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Balance balance;

    protected Entry() {
    }

    public Entry(String id, float entryAmount, Balance balance) {
        this.setId(id);
        this.setEntryAmount(entryAmount);
        this.setBalance(balance);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getEntryAmount() {
        return entryAmount;
    }

    public void setEntryAmount(float entryAmount) {
        this.entryAmount = entryAmount;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return getId() + "," + getEntryAmount() + "," + getBalance().toString();
    }
}
