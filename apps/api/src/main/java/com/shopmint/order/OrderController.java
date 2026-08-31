package com.shopmint.order;

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
            @RequestParam String couponCode,
            @RequestParam ShippingType shippingType) {

        return orderService.placeOrder(
                customerId,
                couponCode,
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

    @PostMapping("/{id}/pay")
    public Order payOrder(@PathVariable int id,
                          @RequestParam PaymentType paymentType) {
        return orderService.payOrder(id, paymentType);
    }

    @PostMapping("/{id}/cancel")
    public Order cancelOrder(@PathVariable int id) {
        return orderService.cancelOrder(id);
    }

    @PostMapping("/{id}/ship")
    public Order shipOrder(@PathVariable int id) {
        return orderService.shipOrder(id);
    }

    @PostMapping("/{id}/deliver")
    public Order deliverOrder(@PathVariable int id) {
        return orderService.deliverOrder(id);
    }
}