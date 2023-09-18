package se.lu.ics;

public class Product {
    // Attributes
    private String id;
    private String name;
    private String category;

    // Constructor
    public Product(int id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    // Setter methods
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}



