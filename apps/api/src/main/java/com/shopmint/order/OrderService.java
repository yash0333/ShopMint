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
import com.shopmint.product.ProductService;
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
    private final ProductService productService;

    private int orderId = 1;
    private boolean festivalSaleActive = true;

    public OrderService(CustomerService customerService,
                        CartService cartService,
                        ProductService productService) {
        this.customerService = customerService;
        this.cartService = cartService;
        this.productService = productService;
    }

    public Order placeOrder(int customerId,
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

                throw new RuntimeException("Insufficient quantity for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    cartItem.getQuantity()
            );

            orderItems.add(orderItem);

            totalAmount += product.getPrice() * cartItem.getQuantity();
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
            throw new RuntimeException("Order amount cannot be negative");
        }

        order.setFinalAmount(finalAmount);

        // Process payment
        order.setStatus(OrderStatus.PAYMENT_PENDING);

        // Reserve inventory
        reserveInventory(cart.getItems());

        // Save order
        orders.put(order.getId(), order);

        // Clear cart
        cartService.clearCart(customerId);

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

    public Order payOrder(int orderId, PaymentType paymentType) {

        Order order = getOrderOrThrow(orderId);

        if (order.getStatus() != OrderStatus.PAYMENT_PENDING) {
            throw new RuntimeException("Payment can only be completed for orders in PAYMENT_PENDING state");
        }

        double finalAmount = order.getFinalAmount();
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

        // Confirm inventory reservation
        confirmInventory(order);

        order.setStatus(OrderStatus.CONFIRMED);

        // Send notifications
        sendOrderNotification(
                order,
                "Order confirmation email sent",
                "Order confirmation SMS sent"
        );

        return order;
    }

    public Order shipOrder(int orderId) {

        Order order = getOrderOrThrow(orderId);

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed orders can be shipped");
        }

        order.setStatus(OrderStatus.SHIPPED);
        sendOrderNotification(
                order,
                "Order shipping email sent",
                "Order shipping SMS sent"
        );

        return order;
    }

    public Order deliverOrder(int orderId) {

        Order order = getOrderOrThrow(orderId);

        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new RuntimeException("Only shipped orders can be delivered");
        }

        order.setStatus(OrderStatus.DELIVERED);
        sendOrderNotification(
                order,
                "Order delivery email sent",
                "Order delivery SMS sent"
        );
        return order;
    }

    public Order cancelOrder(int orderId) {

        Order order = getOrderOrThrow(orderId);

        if (order.getStatus() != OrderStatus.PAYMENT_PENDING
                && order.getStatus() != OrderStatus.CONFIRMED) {

            throw new RuntimeException("Order cannot be cancelled in " + order.getStatus() + " state");
        }

        // Release inventory reservation
        if (order.getStatus() == OrderStatus.PAYMENT_PENDING) {
            releaseInventory(order);
        } else if (order.getStatus() == OrderStatus.CONFIRMED) {
            restoreInventory(order);
        };

        order.setStatus(OrderStatus.CANCELLED);
        sendOrderNotification(
                order,
                "Order cancellation email sent",
                "Order cancellation SMS sent"
        );

        return order;
    }

    private Order getOrderOrThrow(int orderId) {

        Order order = orders.get(orderId);

        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        return order;
    }

    private void reserveInventory(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.getAvailableQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient quantity for product: " + product.getName());
            }

            product.setAvailableQuantity(
                    product.getAvailableQuantity() - cartItem.getQuantity());

            product.setReservedQuantity(
                    product.getReservedQuantity() + cartItem.getQuantity());
        }
    }

    private void confirmInventory(Order order) {
        for (OrderItem orderItem : order.getItems()) {

            Product product =
                    productService.getProduct(orderItem.getProductId());

            if (product != null) {
                product.setReservedQuantity(
                        product.getReservedQuantity()
                                - orderItem.getQuantity());
            }
        }
    }

    private void releaseInventory(Order order) {
        for (OrderItem orderItem : order.getItems()) {

            Product product =
                    productService.getProduct(orderItem.getProductId());

            if (product != null) {

                product.setAvailableQuantity(
                        product.getAvailableQuantity()
                                + orderItem.getQuantity());

                product.setReservedQuantity(
                        product.getReservedQuantity()
                                - orderItem.getQuantity());
            }
        }
    }

    private void restoreInventory(Order order) {
        for (OrderItem orderItem : order.getItems()) {

            Product product =
                    productService.getProduct(orderItem.getProductId());

            if (product != null) {

                product.setAvailableQuantity(
                        product.getAvailableQuantity()
                                + orderItem.getQuantity());
            }
        }
    }

    private void sendOrderNotification(
            Order order,
            String emailMessage,
            String smsMessage) {

        Customer customer =
                customerService.getCustomer(order.getCustomerId());

        List<OrderNotification> notifications = new ArrayList<>();

        notifications.add(
                new OrderNotification(
                        "EMAIL",
                        customer.getEmail(),
                        emailMessage
                )
        );

        notifications.add(
                new OrderNotification(
                        "SMS",
                        customer.getPhone(),
                        smsMessage
                )
        );

        order.setNotifications(notifications);
    }
}
