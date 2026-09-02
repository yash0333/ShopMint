package com.shopmint.payment;

public interface PaymentStrategy {

    PaymentResult pay(double amount);
}
