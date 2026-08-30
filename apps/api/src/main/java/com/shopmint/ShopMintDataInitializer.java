package com.shopmint;

import com.shopmint.customer.Customer;
import com.shopmint.customer.CustomerService;
import com.shopmint.product.Product;
import com.shopmint.product.ProductCategory;
import com.shopmint.product.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ShopMintDataInitializer implements CommandLineRunner {
    private final CustomerService customerService;
    private final ProductService productService;

    public ShopMintDataInitializer(CustomerService customerService, ProductService productService) {
        this.customerService = customerService;
        this.productService = productService;
    }

    @Override
    public void run(String... args) {
        customerService.registerCustomer(
                new Customer( 1, "Yash", "yash@shopmint.com", "9876543210", "Bangalore" ) );
        customerService.registerCustomer( new Customer( 2, "Rahul", "rahul@shopmint.com", "9876543211", "Mumbai" ) );

        productService.addProduct( new Product( 1, "Laptop", "15-inch laptop with 16GB RAM", ProductCategory.ELECTRONICS, 60000, 10 ) );
        productService.addProduct( new Product( 2, "Wireless Mouse", "Wireless optical mouse", ProductCategory.ELECTRONICS, 1500, 25 ) );
        productService.addProduct( new Product( 3, "Mechanical Keyboard", "Mechanical keyboard with RGB lighting", ProductCategory.ELECTRONICS, 3500, 15 ) );
        productService.addProduct( new Product( 4, "Java Programming Book", "Complete guide to Java programming", ProductCategory.BOOKS, 1200, 20 ) );

        System.out.println("ShopMint sample data initialized.");
    }
}
