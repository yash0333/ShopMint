package com.shopmint.customer;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerService {

    private final Map<Integer, Customer> customers = new HashMap<>();

    public Customer registerCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Customer getCustomer(int customerId) {
        return customers.get(customerId);
    }
}
