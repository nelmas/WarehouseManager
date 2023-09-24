package se.lu.ics.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.models.Product;
import se.lu.ics.models.Supplier; 

public class ProductDAO {
    

     private static ObservableList<Product> products = FXCollections.observableArrayList();

    static {
        updateProductsFromDatabase(); 

    }
        //Getter for product ObservableList
        public static ObservableList<Product> getProducts() {
        return products;
    }
    //Update product from database method
    public static void updateProductsFromDatabase() {
        String query = "SELECT * FROM Product";
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            products.clear(); 

            while (resultSet.next()) {
                String productId = resultSet.getString("ProductId");
                String productName = resultSet.getString("Name");
                String category = resultSet.getString("Category");
                Supplier supplierId = SupplierDAO.getSupplierById(resultSet.getString("SupplierId")); 
                Product product = new Product(productId, productName, category, supplierId);
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Methods ---------------------------------------------
        
    //Static getter for product by id
        public static Product getProductById(String productId) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    //Static getter for product by category
        public static Product getProductByCategory(String productCategory) {
        for (Product product : products) {
            if (product.getProductCategory().equals(productCategory)) {
                return product; 
            }
        } return null; 

    }

       //Method for registering a product
        public void addProduct(Product product) {
            products.add(product);
        }

        // Remove a product from the list
        public void removeProduct(Product product) {
            products.remove(product);
        }

        public static void addProductToDatabase(String productId, String productName, String productCategory, Supplier supplier) throws SQLException {
            String query = "INSERT INTO Product (ProductId, Name, Category, SupplierId) VALUES (?, ?, ?, ?)";
            String supplierId = supplier.getSupplierId();
        
            try (Connection connection = ConnectionHandler.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, productId);
                statement.setString(2, productName);
                statement.setString(3, productCategory);
                statement.setString(4, supplierId);
                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    Product product = new Product(productId, productName, productCategory, supplier);
                // After successfully inserting the product, you can update your local data
                products.add(product);
                }
                updateProductsFromDatabase();
            }
           
        }

        public static void removeProductFromProductTable(Product product) throws SQLException {
            String query = "DELETE FROM Product WHERE ProductId = ?";
        
            try (Connection connection = ConnectionHandler.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, product.getProductId());
        
                statement.executeUpdate();
        
                // After successfully removing the product, update your local data
                products.remove(product);
                 }
        }
            

        public static void updateProductInDatabase(Product updatedProduct) {
            String query = "UPDATE Product SET Name = ?, Category = ?, SupplierId = ? WHERE ProductId = ?";

            try (Connection connection = ConnectionHandler.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, updatedProduct.getProductName());
                statement.setString(2, updatedProduct.getProductCategory());
                statement.setString(3, updatedProduct.getSupplier().getSupplierId());
                statement.setString(4, updatedProduct.getProductId());

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
        }

        // Retrieve unique product categories from the database
    public static ObservableList<String> getProductCategories() {
        ObservableList<String> categories = FXCollections.observableArrayList();

        String query = "SELECT DISTINCT Category FROM Product";

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String category = resultSet.getString("Category");
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return categories;
    }

       

}