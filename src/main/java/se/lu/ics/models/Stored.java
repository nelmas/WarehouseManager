package se.lu.ics.models;

public class Stored {
    private Product product; 
    private Warehouse warehouse; 
    private int stock; 

    //Constructor
    public Stored(Product product, Warehouse warehouse, int stock) {
        this.product = product;
        this.warehouse = warehouse;
        this.stock = stock;
    }
    //Getters & Setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }
    public Warehouse getWarehouse() {
        return warehouse;
    }
    public void setStock(int stock) {
        this.stock = stock;  
    }
    public int getStock() {
        return stock;
    }
    
}
