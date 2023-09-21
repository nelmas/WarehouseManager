package se.lu.ics.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    // Attributes
    private String productId;
    private String productName;
    private String productCategory;

    private ObservableList<Stored> stockList = FXCollections.observableArrayList();

    // Constructor
    public Product(String productId, String productName, String productCategory) {
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
    }

    // Getter methods
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
   
}



