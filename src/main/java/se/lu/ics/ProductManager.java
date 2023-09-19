package se.lu.ics;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    private List<Product> products;
    private List<Warehouse> warehouses;
    private List<Supplier> suppliers;

    public ProductManager() {
        // Initialize an empty list of products
        products = new ArrayList<>();
        warehouses = new ArrayList<>();
        suppliers = new ArrayList<>();

    }

    // Add a product to the list
    public void addProduct(Product product) {
        products.add(product);
    }

    public void addWarehouse(Warehouse warehouse) {
        warehouses.add(warehouse);
    }

    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
    }
    
    public List<Warehouse> getAllWarehouses() {
            return warehouses;
       
    }

    public List<Supplier> getAllSuppliers() {
        return suppliers;
    }

    // Retrieve a product by its ID
    public Product getProductById(String productId) {
        for (Product product : products) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null; // Product with the given ID not found
    }

    // Register a new product
    public void registerProduct(String productId, String name, String category) {
        Product newProduct = new Product(productId, name, category);
        addProduct(newProduct);
    }

    

    //Retrieve products by category
    public List<Product> getProductsByCategory(String category) {
        List<Product> productsInCategory = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equalsIgnoreCase(category)) {
                productsInCategory.add(product);
            }
        }
        return productsInCategory;
    }

    public Supplier getSupplierById(String supplierId) {
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierId() == supplierId) {
                return supplier;
            }
        }
        return null;
    }

    // Display total stock for a certain product across all warehouses
    // Behöver vi lägga till attribut stock i Product så denna funktion kan fungera?
    public int getTotalStockForProduct(String productId) {
        int totalStock = 0;
        for (Product product : products) {
            if (product.getProductId() == productId) {
                totalStock += product.getStock();
            }
        }
        return totalStock;
    }

    // Retrieve total stock for a product (by product ID)
    public int getTotalStockForProduct(String productId) {
        int totalStock = 0;
        for (Product product : products) {
            if (product.getProductId() == productId) {
                totalStock += product.getStock();
            }
        }
        return totalStock;
    }

    public boolean removeProduct(String productId) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                products.remove(product);
                return true; // Product removed successfully
            }
        }
        return false; // Product with the given ID not found
    }

    // Remove a supplier by specifying its ID
    public boolean removeSupplier(String supplierId) {
        Supplier supplierToRemove = null;
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierId() == supplierId) {
                supplierToRemove = supplier;
                break;
            }
        }
        
        if (supplierToRemove != null) {
            suppliers.remove(supplierToRemove);
            return true; // Supplier removed successfully
        } else {
            return false; // Supplier with the given ID not found
        }
    }
}

