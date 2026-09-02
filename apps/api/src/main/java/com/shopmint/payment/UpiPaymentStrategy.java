package com.shopmint.payment;


public class UpiPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentResult pay(double amount) {
        return new PaymentResult(
                PaymentType.UPI.name(),
                true,
                "Processing UPI payment: ₹" + amount
        );
    }
}
