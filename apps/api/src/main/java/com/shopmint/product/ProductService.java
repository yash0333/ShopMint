package com.shopmint.product;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final Map<Integer, Product> products = new HashMap<>();

    public Product addProduct(Product product) {
        products.put(product.getId(), product);
        return product;
    }

    public Product getProduct(int productId) {
        return products.get(productId);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public List<Product> searchProducts(String keyword) {

        List<Product> result = new ArrayList<>();

        for (Product product : products.values()) {

            if (product.getName().toLowerCase()
                    .contains(keyword.toLowerCase())) {

                result.add(product);
            }
        }

        return result;
    }
}
