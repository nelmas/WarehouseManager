package se.lu.ics.data;

import se.lu.ics.models.Product;
import se.lu.ics.models.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SupplierDAO {
    private static ObservableList<Supplier> suppliers = FXCollections.observableArrayList();

    static {
        updateSuppliersFromDatabase();

    }

    // Getter for suppliers ObservableList
    public static ObservableList<Supplier> getSuppliers() {
        return suppliers;
    }

    // Update supplier from database method
    public static void updateSuppliersFromDatabase() {
        String query = "SELECT * FROM Supplier";
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            suppliers.clear();
            while (resultSet.next()) {
                String supplierId = resultSet.getString("SupplierId");
                String name = resultSet.getString("Name");
                String address = resultSet.getString("Address");
                String email = resultSet.getString("Email");
                Supplier supplier = new Supplier(name, supplierId, address, email);
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method for registering a supplier
    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
    }

    // add supplier to database
    public static void addSupplierToDatabase(String name, String supplierId, String address, String email)
            throws SQLException {
        String query = "INSERT INTO Supplier (Name, SupplierId, Address, Email) VALUES (?, ?, ?, ?)";

        Connection connection = ConnectionHandler.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        {

            statement.setString(1, name);
            statement.setString(2, supplierId);
            statement.setString(3, address);
            statement.setString(4, email);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                Supplier supplier = new Supplier(name, supplierId, address, email);
                suppliers.add(supplier); // Make sure suppliers is correctly initialized
            }
        }

    }

    public static void removeSupplierFromDatabase(Supplier supplier) throws SQLException {
        String query = "DELETE Supplier WHERE SupplierId = ?";

        try (Connection connection = ConnectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, supplier.getSupplierId());

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                // Successfully deleted from the database; now remove from the local list
                suppliers.remove(supplier);
            }
        }
    }

    public static void updateSupplierInDatabase(Supplier supplier) {
        String query = "UPDATE Supplier SET Name = ?, Address = ?, Email = ? WHERE SupplierId = ?";

        try (Connection connection = ConnectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, supplier.getSupplierName());
            statement.setString(2, supplier.getSupplierAddress());
            statement.setString(3, supplier.getSupplierEmail());
            statement.setString(4, supplier.getSupplierId());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                // Successfully updated in the database
                // No need to update the local list since it's already updated
            } else {
                // Handle the case where the supplier was not found in the database
                // (e.g., show an error message to the user)
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the error (e.g., show an error message to the user)
        }
    }

    // Static getter for Supplier by ID
    public static Supplier getSupplierById(String supplierId) {
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierId().equals(supplierId)) {
                return supplier;
            }
        }
        return null;
    }

    public static ObservableList<Product> getSuppliedProducts(Supplier supplier) {
        ObservableList<Product> suppliedProducts = FXCollections.observableArrayList();
        String supplierId = supplier.getSupplierId(); // Get the supplier's ID

        // Query the database to retrieve products supplied by the specified supplier
        String query = "SELECT * FROM Product WHERE SupplierId = ?";
        try (Connection connection = ConnectionHandler.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, supplierId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String productId = resultSet.getString("ProductId");
                String productName = resultSet.getString("Name");
                String category = resultSet.getString("Category");
                Product product = new Product(productId, productName, category, supplier);
                suppliedProducts.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return suppliedProducts;
    }

    public static ObservableList<String> getAllSupplierIds() {
        ObservableList<String> supplierIds = FXCollections.observableArrayList();
        for (Supplier supplier : suppliers) {
            supplierIds.add(supplier.getSupplierId());
        }
        return supplierIds;
    }

}
