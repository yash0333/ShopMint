package com.shopmint.order;

import com.shopmint.discount.DiscountType;
import com.shopmint.payment.PaymentType;
import com.shopmint.shipping.ShippingType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order placeOrder(
            @RequestParam int customerId,
            @RequestParam PaymentType paymentType,
            @RequestParam DiscountType discountType,
            @RequestParam ShippingType shippingType) {

        return orderService.placeOrder(
                customerId,
                paymentType,
                discountType,
                shippingType
        );
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable int id) {
        return orderService.getOrder(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<Order> getCustomerOrders(
            @PathVariable int customerId) {

        return orderService.getCustomerOrders(customerId);
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable int id) {

        orderService.cancelOrder(id);

        return "Order cancelled";
    }
}