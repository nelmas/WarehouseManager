package se.lu.ics;

public class Product {
    // Attributes
    private String productId;
    private String name;
    private String category;

    // Constructor
    public Product(String productId, String name, String category) {
        this.productId = productId;
        this.name = name;
        this.category = category;
    }

    // Getter methods
    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    // Setter methods
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}



