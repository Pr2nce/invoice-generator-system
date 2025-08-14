public class InvoiceItem {
    private String name;
    private int quantity;
    private double totalPrice;

    public InvoiceItem(String name, int quantity, double totalPrice) {
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // Getters
    public String getName() {
        return name;
    }
    public int getQuantity() {
        return quantity;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
}