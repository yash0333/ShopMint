package com.shopmint.cart;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int id;
    private int customerId;
    private List<CartItem> items = new ArrayList<>();

    public Cart(int id, int customerId) {
        this.id = id;
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
