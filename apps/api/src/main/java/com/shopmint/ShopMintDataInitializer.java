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

        addCustomers();

        addProducts();

        System.out.println("ShopMint sample data initialized.");
    }

    private void addCustomers() {
        customerService.registerCustomer(
                new Customer( 1, "Yash", "yash@shopmint.com", "9876543210", "Bangalore" )
        );
        customerService.registerCustomer(
                new Customer( 2, "Rahul", "rahul@shopmint.com", "9876543211", "Mumbai" )
        );
    }

    private void addProducts(){
        productService.addProduct(
                new Product(
                        1,
                        "Laptop",
                        "15-inch laptop with 16GB RAM",
                        ProductCategory.ELECTRONICS,
                        60000,
                        10
                )
        );

        productService.addProduct(
                new Product(
                        2,
                        "Wireless Mouse",
                        "Wireless optical mouse",
                        ProductCategory.ELECTRONICS,
                        1500,
                        25
                )
        );

        productService.addProduct(
                new Product(
                        3,
                        "Mechanical Keyboard",
                        "Mechanical keyboard with RGB lighting",
                        ProductCategory.ELECTRONICS,
                        3500,
                        15
                )
        );

        productService.addProduct(
                new Product(
                        4,
                        "Java Programming Book",
                        "Complete guide to Java programming",
                        ProductCategory.BOOKS,
                        1200,
                        20
                )
        );

        productService.addProduct(
                new Product(
                        5,
                        "Spring Boot in Action",
                        "Practical guide to building Spring Boot applications",
                        ProductCategory.BOOKS,
                        1800,
                        15
                )
        );

        productService.addProduct(
                new Product(
                        6,
                        "USB-C Hub",
                        "7-in-1 USB-C hub with HDMI and USB ports",
                        ProductCategory.ELECTRONICS,
                        2500,
                        20
                )
        );

        productService.addProduct(
                new Product(
                        7,
                        "Laptop Backpack",
                        "Water-resistant backpack for laptops up to 15 inches",
                        ProductCategory.ACCESSORIES,
                        2200,
                        18
                )
        );

        productService.addProduct(
                new Product(
                        8,
                        "Webcam",
                        "Full HD webcam with built-in microphone",
                        ProductCategory.ELECTRONICS,
                        4500,
                        12
                )
        );
    }
}
