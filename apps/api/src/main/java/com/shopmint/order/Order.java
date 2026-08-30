package com.shopmint.order;

import com.shopmint.notification.OrderNotification;
import com.shopmint.payment.PaymentResult;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private int id;
    private int customerId;
    private List<OrderItem> items = new ArrayList<>();

    private double totalAmount;
    private double discountAmount;
    private double shippingAmount;
    private double finalAmount;

    private OrderStatus status;

    private PaymentResult paymentResult;
    private List<OrderNotification> notifications = new ArrayList<>();

    public Order(int id, int customerId) {
        this.id = id;
        this.customerId = customerId;
        this.status = OrderStatus.CREATED;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(double shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public List<OrderNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<OrderNotification> notifications) {
        this.notifications = notifications;
    }

    public PaymentResult getPaymentResult() {
        return paymentResult;
    }

    public void setPaymentResult(PaymentResult paymentResult) {
        this.paymentResult = paymentResult;
    }
}
