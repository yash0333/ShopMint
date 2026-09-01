package com.shopmint.cart;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{customerId}")
    public Cart getCart(@PathVariable int customerId) {
        return cartService.getCart(customerId);
    }

    @PostMapping("/{customerId}/items")
    public String addToCart(
            @PathVariable int customerId,
            @RequestParam int productId) {

        cartService.addToCart(customerId, productId);

        return "Product added to cart";
    }

    @DeleteMapping("/{customerId}/items/{productId}")
    public String removeFromCart(
            @PathVariable int customerId,
            @PathVariable int productId) {

        cartService.removeFromCart(customerId, productId);

        return "Product removed from cart";
    }

    @DeleteMapping("/{customerId}")
    public String clearCart(@PathVariable int customerId) {

        cartService.clearCart(customerId);

        return "Cart cleared";
    }
}
