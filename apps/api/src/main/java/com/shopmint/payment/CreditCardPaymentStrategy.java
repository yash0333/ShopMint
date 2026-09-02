package com.shopmint.payment;

public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult pay(double amount) {
        return new PaymentResult(
                PaymentType.CREDIT_CARD.name(),
                true,
                "Processing Credit Card payment: ₹" + amount
        );
    }
}
