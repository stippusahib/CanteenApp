package com.canteen.model;

public class MenuItem {
    private int itemId;
    private String name;
    private double price;

    public MenuItem(int itemId, String name, double price) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
    }

    public int getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
