package se.lu.ics.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    // Attributes
    private String productId;
    private String productName;
    private String productCategory;
    private Supplier supplier; 

    private ObservableList<Stored> stockList = FXCollections.observableArrayList();

    // Constructor
    public Product(String productId, String productName, String productCategory, Supplier supplier) {
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.supplier = supplier; 
    }

    // Getter methods

    public Supplier getSupplier() {
        return supplier;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    // Setter methods
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
    public void setProductId(String id) {
        this.productId = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
    public void addStock(Stored stock) {
        stockList.add(stock);
        
    }
    public String getSupplierId () {
        return supplier.getSupplierId();
    }
   
}



