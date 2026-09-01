package com.shopmint.cart;

import com.shopmint.product.Product;
import com.shopmint.product.ProductService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {

    private final Map<Integer, Cart> carts = new HashMap<>();

    private final ProductService productService;

    public CartService(ProductService productService) {
        this.productService = productService;
    }

    public Cart getCart(int customerId) {

        if (!carts.containsKey(customerId)) {
            carts.put(customerId, new Cart(customerId, customerId));
        }

        return carts.get(customerId);
    }

    public void addToCart(int customerId, int productId) {

        Product product = productService.getProduct(productId);

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        Cart cart = getCart(customerId);

        for (CartItem item : cart.getItems()) {

            if (item.getProduct().getId() == productId) {

                int requestedQuantity = item.getQuantity() + 1;

                if (requestedQuantity > product.getAvailableQuantity()) {
                    throw new RuntimeException("Insufficient product quantity");
                }

                item.setQuantity(requestedQuantity);
                return;
            }
        }

        if (product.getAvailableQuantity() <= 0) {
            throw new RuntimeException("Insufficient product quantity");
        }

        cart.getItems().add(new CartItem(product, 1));
    }

    public void removeFromCart(int customerId, int productId) {

        Cart cart = getCart(customerId);

        cart.getItems().removeIf(
                item -> item.getProduct().getId() == productId
        );
    }

    public void clearCart(int customerId) {
        getCart(customerId).getItems().clear();
    }

    public double calculateCartTotal(int customerId) {

        Cart cart = getCart(customerId);

        double total = 0;

        for (CartItem item : cart.getItems()) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }

        return total;
    }
}
