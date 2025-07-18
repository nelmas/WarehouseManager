package se.lu.ics.models;


import java.util.Objects;

import se.lu.ics.data.StoredDAO;


public class Stored {
    private Product product;
    private Warehouse warehouse;
    private int stock;

    // Constructor
    public Stored(Product product, Warehouse warehouse, int stock) {
        this.product = product;
        this.warehouse = warehouse;
        this.stock = stock;
        warehouse.addStock(this);
        product.addStock(this);
    }

    // Getters & Setters
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

    public String getProductId() {
         return product.getProductId();
    }

    public String getProductName() {
        return product.getProductName();
    }
    public String getSupplierId() {
        return product.getSupplierId();
    }
    public String getProductCategory() {
        return product.getProductCategory();
    }


    public String getWarehouseId() {
        return warehouse.getWarehouseId();
    }
    

}
