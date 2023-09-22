package se.lu.ics.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.models.Product;
import se.lu.ics.models.Stored;
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

        public static void addProductToDatabase(Product product) {
            String query = "INSERT INTO Product (ProductId, Name, Category, SupplierId) VALUES (?, ?, ?, ?)";
        
            try (Connection connection = ConnectionHandler.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, product.getProductId());
                statement.setString(2, product.getProductName());
                statement.setString(3, product.getProductCategory());
                statement.setString(4, product.getSupplier().getSupplierId());
        
                statement.executeUpdate();
        
                // After successfully inserting the product, you can update your local data
                products.add(product);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
        }

        public static void removeProductFromDatabase(Product product) {
            String query = "DELETE FROM Product WHERE ProductId = ?";
        
            try (Connection connection = ConnectionHandler.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, product.getProductId());
        
                statement.executeUpdate();
        
                // After successfully removing the product, update your local data
                products.remove(product);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
        }

}