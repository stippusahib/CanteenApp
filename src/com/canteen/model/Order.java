package com.canteen.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<OrderItem> items;

    public Order() {
        this.items = new ArrayList<>();
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getSubtotal() {
        double subtotal = 0.0;
        for (OrderItem item : items) {
            subtotal += item.getTotalPrice();
        }
        return subtotal;
    }
}
