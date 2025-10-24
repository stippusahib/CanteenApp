package com.canteen.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    
    public double getSubtotal() {
        double subtotal = 0.0;
        for (OrderItem item : items) {
            
            subtotal += item.getTotal();
        }
        return subtotal;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}
