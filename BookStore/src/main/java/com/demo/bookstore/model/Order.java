package com.demo.bookstore.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing an Order in the BookStore
 */
public class Order {
    private int id;
    private int customerId;
    private List<CartItem> items;
    private double totalPrice;

    public Order() {
        this.items = new ArrayList<>();
    }

    public Order(int id, int customerId, List<CartItem> items, double totalPrice) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.totalPrice = totalPrice;
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
} 