package com.shopmint.order;

import com.shopmint.cart.Cart;
import com.shopmint.cart.CartItem;
import com.shopmint.cart.CartService;
import com.shopmint.customer.Customer;
import com.shopmint.customer.CustomerService;
import com.shopmint.discount.DiscountType;
import com.shopmint.notification.OrderNotification;
import com.shopmint.payment.PaymentResult;
import com.shopmint.payment.PaymentType;
import com.shopmint.product.Product;
import com.shopmint.shipping.ShippingType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final Map<Integer, Order> orders = new HashMap<>();

    private final CustomerService customerService;
    private final CartService cartService;

    private int orderId = 1;
    private boolean festivalSaleActive = true;

    public OrderService(CustomerService customerService,
                        CartService cartService) {
        this.customerService = customerService;
        this.cartService = cartService;
    }

    public Order placeOrder(int customerId,
                            PaymentType paymentType,
                            String couponCode,
                            ShippingType shippingType) {

        // Validate customer
        Customer customer = customerService.getCustomer(customerId);

        if (customer == null) {
            throw new RuntimeException("Customer not found");
        }

        // Get cart
        Cart cart = cartService.getCart(customerId);

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Create order
        Order order = new Order(orderId++, customerId);

        List<OrderItem> orderItems = new ArrayList<>();

        double totalAmount = 0;

        // Add cart items to order
        for (CartItem cartItem : cart.getItems()) {

            Product product = cartItem.getProduct();

            if (product.getAvailableQuantity()
                    < cartItem.getQuantity()) {

                throw new RuntimeException(
                        "Insufficient quantity for product: "
                                + product.getName());
            }

            OrderItem orderItem = new OrderItem(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    cartItem.getQuantity()
            );

            orderItems.add(orderItem);

            totalAmount += product.getPrice()
                    * cartItem.getQuantity();
        }

        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        // Apply discount
        double discountAmount = 0;

        // 1. Order value discount - 10% for orders >= ₹5,000
        if (totalAmount >= 5000) {
            discountAmount += totalAmount * 0.10;
        }

        // 2. Large order discount - ₹500 for orders >= ₹10,000
        if (totalAmount >= 10000) {
            discountAmount += 500;
        }

        // 3. Coupon discount - ₹1,000 for valid coupon
        if ("WELCOME1000".equals(couponCode)) {
            discountAmount += 1000;
        }

        // 4. New customer discount - ₹250 on first order
        if (customer.isNewCustomer()) {
            discountAmount += 250;
        }

        // 5. Festival discount - 15% for orders >= ₹20,000
        if (festivalSaleActive && totalAmount >= 20000) {
            discountAmount += totalAmount * 0.15;
        }

        // 6. Maximum discount - cap total discount at ₹5,000
        if (discountAmount > 5000) {
            discountAmount = 5000;
        }

        order.setDiscountAmount(discountAmount);

        double amountAfterDiscount = totalAmount - discountAmount;

        // Calculate shipping
        double shippingAmount = 0;

        if (shippingType == ShippingType.STANDARD) {
            shippingAmount = 50;

        } else if (shippingType == ShippingType.EXPRESS) {
            shippingAmount = 150;
        }

        order.setShippingAmount(shippingAmount);

        double finalAmount =
                amountAfterDiscount + shippingAmount;

        if (finalAmount < 0) {
            throw new IllegalArgumentException("Order amount cannot be negative");
        }

        order.setFinalAmount(finalAmount);

        // Process payment
        order.setStatus(OrderStatus.PAYMENT_PENDING);

        PaymentResult paymentResult = null;

        if (paymentType == PaymentType.CREDIT_CARD) {

            paymentResult = new PaymentResult(
                    paymentType.name(),
                    true,
                    "Processing Credit Card payment: ₹" + finalAmount
            );

        } else if (paymentType == PaymentType.DEBIT_CARD) {

            paymentResult = new PaymentResult(
                    paymentType.name(),
                    true,
                    "Processing Debit Card payment: ₹" + finalAmount
            );

        } else if (paymentType == PaymentType.UPI) {

            paymentResult = new PaymentResult(
                    paymentType.name(),
                    true,
                    "Processing UPI payment: ₹" + finalAmount
            );

        } else if (paymentType == PaymentType.COD) {

            paymentResult = new PaymentResult(
                    paymentType.name(),
                    true,
                    "Cash on Delivery selected"
            );
        }

        if (paymentResult == null) {
            throw new RuntimeException(
                    "Unsupported payment type: " + paymentType);
        }

        order.setPaymentResult(paymentResult);

        // Update inventory
        for (CartItem cartItem : cart.getItems()) {

            Product product = cartItem.getProduct();

            product.setAvailableQuantity(
                    product.getAvailableQuantity()
                            - cartItem.getQuantity()
            );
        }

        // Confirm order
        order.setStatus(OrderStatus.CONFIRMED);

        // Save order
        orders.put(order.getId(), order);

        // Clear cart
        cartService.clearCart(customerId);

        // Send notifications
        List<OrderNotification> notifications = new ArrayList<>();

        notifications.add(
                new OrderNotification(
                        "EMAIL",
                        customer.getEmail(),
                        "Order confirmation email sent"
                )
        );

        notifications.add(
                new OrderNotification(
                        "SMS",
                        customer.getPhone(),
                        "Order confirmation SMS sent"
                )
        );

        order.setNotifications(notifications);

        return order;
    }

    public Order getOrder(int orderId) {
        return orders.get(orderId);
    }

    public List<Order> getCustomerOrders(int customerId) {

        List<Order> customerOrders = new ArrayList<>();

        for (Order order : orders.values()) {

            if (order.getCustomerId() == customerId) {
                customerOrders.add(order);
            }
        }

        return customerOrders;
    }

    public void cancelOrder(int orderId) {

        Order order = orders.get(orderId);

        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException(
                    "Delivered order cannot be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);

        System.out.println(
                "Order " + orderId + " cancelled");
    }
}
