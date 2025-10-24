package com.canteen.model;

public class Bill {
    private Order order;
    private static final double TAX_RATE = 0.05; // 5% tax rate

    public Bill(Order order) {
        this.order = order;
    }

    public double getTax() {
       
        return order.getSubtotal() * TAX_RATE;
    }

    public double getTotal() {
       
        return order.getSubtotal() + getTax();
    }

    public String generateBillText() {
        double subtotal = order.getSubtotal();
        double tax = getTax();
        double total = getTotal();
        
        
        return String.format(
            "---------- CANTEEN BILL SUMMARY ----------\n" +
            "Subtotal: $%.2f\n" +
            "Tax (%.0f%%):    $%.2f\n" +
            "------------------------------------------\n" +
            "TOTAL:    $%.2f\n" +
            "------------------------------------------\n",
            subtotal, 
            TAX_RATE * 100, 
            tax, 
            total
        );
    }
}
