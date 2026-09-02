package com.shopmint.payment;

public class DebitCardPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult pay(double amount) {
        return new PaymentResult(
                PaymentType.DEBIT_CARD.name(),
                true,
                "Processing Debit Card payment: ₹" + amount
        );
    }
}
