package com.shopmint.payment;

public class PaymentResult {

    private String paymentType;
    private boolean successful;
    private String message;

    public PaymentResult(String paymentType,
                         boolean successful,
                         String message) {
        this.paymentType = paymentType;
        this.successful = successful;
        this.message = message;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
