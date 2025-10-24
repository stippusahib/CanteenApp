package com.canteen.model;

public class Bill {
    private static final double TAX_RATE = 0.05;
    protected Order order;

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
        String bill = "";
        
        bill = bill + "===== CANTEEN BILL =====\n";
        bill = bill + "Item          Qty  Price  Total\n";
        bill = bill + "--------------------------------\n";

        // Loop through items 
        for (OrderItem item : order.getItems()) {
            bill = bill + item.getName() + "  " 
                        + item.getQuantity() + "   " 
                        + item.getPrice() + "  " 
                        + item.getTotal() + "\n";
        }

        bill = bill + "--------------------------------\n";
        
        bill = bill + "Subtotal:        " + order.getSubtotal() + "\n";
        bill = bill + "Tax (5%):        " + getTax() + "\n";
        bill = bill + "FINAL TOTAL:     " + getTotal() + "\n";
        bill = bill + "================================";

        return bill;
    }
}
