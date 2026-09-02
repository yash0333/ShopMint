package com.shopmint.payment;

public class CodPaymentStrategy implements PaymentStrategy{

    @Override
    public PaymentResult pay(double amount) {
        return new PaymentResult(
                PaymentType.COD.name(),
                true,
                "Cash on Delivery selected"
        );
    }
}
