package se.lu.ics.models;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private List<Product> products;

    public ProductManager() {
        // Initialize an empty list of products
        products = new ArrayList<>();
    }

    // Add a product to the list
    public void addProduct(Product product) {
        products.add(product);
    }

    // Retrieve a product by its ID
    public Product getProductById(String id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null; // Product with the given ID not found
    }

    // Register a new product
    public void registerProduct(String id, String name, String category) {
        Product newProduct = new Product(id, name, category);
        addProduct(newProduct);
    }
    // Find product by id
    public static Product getProductById(String id, List<Product> productList) {
        for (Product product : productList) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null; // Product with the given ID not found
    }


}
