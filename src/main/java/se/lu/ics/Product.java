package se.lu.ics;

import java.util.ArrayList;
import java.util.List;

public class Product {
    // Attributes
    private int id;
    private String name;
    private String category;

    // Constructor
    public Product(int id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    // Setter methods
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static Product getProductById(int id, List<Product> productList) {
        for (Product product : productList) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null; // Product with the given ID not found
    }

}



